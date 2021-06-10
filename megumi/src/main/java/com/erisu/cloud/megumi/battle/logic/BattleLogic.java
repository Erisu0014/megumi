package com.erisu.cloud.megumi.battle.logic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erisu.cloud.megumi.battle.mapper.*;
import com.erisu.cloud.megumi.battle.pojo.*;
import com.erisu.cloud.megumi.battle.util.BattleFormat;
import com.erisu.cloud.megumi.battle.util.DamageType;
import com.erisu.cloud.megumi.exception.MegumiException;
import com.erisu.cloud.megumi.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.util.MessageChainPo;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.ExceptionInEventHandlerException;
import net.mamoe.mirai.event.events.GroupEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description 公会战逻辑处理
 * @Author alice
 * @Date 2021/6/4 9:06
 **/
@Slf4j
@Component
public class BattleLogic {
    @Resource
    private PluginLogic pluginLogic;
    @Resource
    private BattleGroupMapper battleGroupMapper;
    @Resource
    private BattleBossMapper battleBossMapper;
    @Resource
    private BattleStageMapper battleStageMapper;
    @Resource
    private BattleDamageMapper battleDamageMapper;
    @Resource
    private NowBossMapper nowBossMapper;
    @Resource
    private BattleUserMapper battleUserMapper;

    public Boolean isEnabled(String name, long id) {
        List<GroupPlugin> plugin = pluginLogic.getGroupPluginByName(name, String.valueOf(id));
        return plugin != null && plugin.size() == 1 && plugin.get(0).getEnabled() == 1;
    }

    /**
     * 判断是否创建工会
     *
     * @param id 群组id
     * @return
     */
    public Boolean isCreatedGroup(long id) {
        BattleGroup battleGroup = battleGroupMapper.selectById(id);
        return battleGroup != null;
    }

    public Boolean createBattleGroup(Group group) {
        if (isCreatedGroup(group.getId())) {
            return true;
        }
        return battleGroupMapper.insert(new BattleGroup(String.valueOf(group.getId()), group.getName())) > 0;
    }

    public List<NowBoss> getNowBossQuery(long id) {
        return nowBossMapper.selectNowBoss(id);
    }

    /**
     * 初始化boss，并返回本次初始化的boss，避免二次查找
     *
     * @param id
     * @return
     */
    public List<BattleBoss> initBoss(long id) {
        // 初始化默认为一阶段
        List<BattleBoss> bosses = searchStageBoss(1);
        int result = nowBossMapper.insertStageBoss(bosses, String.valueOf(id), 1, 1);
        if (result <= 0) {


            log.warn("插入失败，boss信息为:{}", JSON.toJSONString(bosses));
            return new ArrayList<>();
        } else {
            return bosses;
        }
    }

    /**
     * 查询某阶段的所有boss
     *
     * @param Stage
     * @return
     */
    private List<BattleBoss> searchStageBoss(int Stage) {
        BattleStage battleStage = battleStageMapper.selectById(Stage);
        QueryWrapper<BattleBoss> wrapper = new QueryWrapper<>();
        wrapper.in(true, "id", battleStage.getBoss1Id(), battleStage.getBoss2Id(),
                battleStage.getBoss3Id(), battleStage.getBoss4Id(), battleStage.getBoss5Id());
        return battleBossMapper.selectList(wrapper);

    }


    public void addBattleUser(User sender, long groupId) {
//        QueryWrapper<BattleUser> wrapper = new QueryWrapper<>();
//        wrapper.eq(true, "qq_id", sender.getId());
//        wrapper.eq(true, "group_id", groupId);
        BattleUser battleUser = new BattleUser(String.valueOf(sender.getId()), sender.getNick(), String.valueOf(groupId), 3);
        battleUserMapper.insertIgnore(battleUser);
    }

    /**
     * 将所有群成员加入工会，不包括本bot
     *
     * @param members
     * @param groupId
     * @return
     */
    public void addAllBattleUser(ContactList<NormalMember> members, long groupId) {
        String groupIdStr = String.valueOf(groupId);
        List<BattleUser> battleUsers = new ArrayList<>();
        members.forEach(m -> {
            battleUsers.add(new BattleUser(String.valueOf(m.getId()), m.getNameCard(), groupIdStr, 3));
        });
        battleUserMapper.insertAllIgnore(battleUsers);


    }


    /**
     * @param sender
     * @param messageChain
     * @param group
     * @param checkCompleted 判断是否为整刀    // TODO: 2021/6/8 后续优化
     * @return
     */
    // TODO: 2021/6/8 后续进行sql分离
    // TODO: 2021/6/8 md事务没回滚 
    @Transactional
    public String fuckBoss(User sender, MessageChain messageChain, Group group, Boolean checkCompleted) throws Exception {
        /*
         1.判断是否为自己的刀
         2.判断打的是什么boss
         3.判断是否尾刀
         4.判断是否有刀
         5.判断是否当前轮次结束，注入新一轮boss
         6.尾刀改变预约状态
         */
        // 0.初始化参数
        String fuckResult;
        String groupId = String.valueOf(group.getId());
        String senderId = String.valueOf(sender.getId());
        String messageJsonString = MessageChain.serializeToJsonString(messageChain);
        List<Object> messageObjects = JSON.parseArray(messageJsonString, Object.class);

        if (!CollUtil.isNotEmpty(messageObjects) || messageObjects.get(0) == null) {
            return "唔，出问题了，联系爱丽丝姐姐看看吧";
        }
        MessageChainPo messageChainPo = JSONObject.parseObject(JSON.toJSONString(messageObjects.get(0)), MessageChainPo.class);
        List<MessageChainPo.MessageInfo> originalMessage = messageChainPo.getOriginalMessage();
        // 1.判断是否为自己的刀
        BattleUser battleUser = getBattleUser(originalMessage, senderId, groupId);
        // 2.判断是几号boss
        DamagedBoss damagedBoss = getDamagedBoss(originalMessage, groupId);
        // 3.判断是否尾刀
        double lost;
        boolean isLast;
        DamageType damageType;
        if (damagedBoss.getNowBoss().getHpNow() <= damagedBoss.getDamage()) {
            // 说明这要出尾刀
            damagedBoss.setDamage(damagedBoss.getNowBoss().getHpNow());//对damage重新赋值
            lost = 0.5;
            damageType = DamageType.Last;
            isLast = true;
        } else {
            // 说明打不死，但不表示不是补偿刀
            if (Math.floor(damagedBoss.getNowBoss().getHpNow()) == damagedBoss.getNowBoss().getHpNow()) {
                lost = 1;
                damageType = DamageType.Complete;
            } else {
                lost = 0.5;
                damageType = DamageType.Incomplete;
            }

            isLast = false;
        }
        //  说明现在没刀了
        if (battleUser.getDamageTimes() - lost < 0) {
            if (!battleUser.getQqId().equals(senderId)) {
                return String.format("你再想想！%s已经出完三刀下班了哟~", battleUser.getNickname());
            } else {
                return "你再想想！你已经出完三刀下班了哟~";
            }
        }
        // 判断尾刀
        if (isLast) {
            damagedBoss.getNowBoss().setHpNow(0);
            updateDamageInfo(damagedBoss, groupId, battleUser, lost);
            // TODO: 2021/6/8 提醒预约和挂树的
            QueryWrapper<NowBoss> nowBossQueryWrapper = new QueryWrapper<>();
            nowBossQueryWrapper.eq("boss_rounds", damagedBoss.getNowBoss().getBossRounds());
            nowBossQueryWrapper.eq("group_id", damagedBoss.getNowBoss().getGroupId());
            List<NowBoss> nowRoundBosses = nowBossMapper.selectList(nowBossQueryWrapper);
            if (CollUtil.isEmpty(nowRoundBosses)) {
                throw new Exception("当前轮boss为空");
            }
            if (nowRoundBosses.stream().allMatch(b -> b.getHpNow() == 0)) {
                // 说明这轮boss死完了，需要注入新一轮boss
                DamagedBoss nextDamagedBoss = insertNewRoundBosses(damagedBoss, groupId);
                fuckResult = BattleFormat.INSTANCE.fuckBossLastInfo(damageType, battleUser.getNickname(), nextDamagedBoss, battleUser.getDamageTimes());
            } else {
                // TODO: 2021/6/8 是否优化为显示所有当前论boss信息,+1的话5号boss怎么办啊
                nowRoundBosses.sort(Comparator.comparingInt(NowBoss::getBossOrder));
                NowBoss nextBoss = nowRoundBosses.stream().filter(b -> b.getHpNow() != 0).findFirst().orElse(null);
                if (nextBoss == null) {
                    throw new Exception("nextBoss为空");
                }
                DamagedBoss nextDamageBoss = new DamagedBoss(damagedBoss.getDamage(), nextBoss);
                fuckResult = BattleFormat.INSTANCE.fuckBossLastInfo(damageType, battleUser.getNickname(), nextDamageBoss, battleUser.getDamageTimes());
            }
        } else {
            // 非尾刀形式
            damagedBoss.getNowBoss().setHpNow(damagedBoss.getNowBoss().getHpNow() - damagedBoss.getDamage());
            updateDamageInfo(damagedBoss, groupId, battleUser, lost);
            fuckResult = BattleFormat.INSTANCE.fuckBossInfo(damageType,
                    battleUser.getNickname(), damagedBoss, battleUser.getDamageTimes());
        }
        battleUserMapper.updateById(battleUser);
        return fuckResult;

    }

    /**
     * 注入新一轮boss
     *
     * @param damagedBoss
     * @param groupId
     * @return
     */
    private DamagedBoss insertNewRoundBosses(DamagedBoss damagedBoss, String groupId) {
        int stage = BattleFormat.INSTANCE.getStage(damagedBoss.getNowBoss().getNowStage() + 1);
        List<BattleBoss> battleBosses = searchStageBoss(stage);
        nowBossMapper.insertStageBoss(battleBosses, String.valueOf(groupId), damagedBoss.getNowBoss().getBossRounds() + 1, stage);
        BattleBoss nextBattleBoss = battleBosses.stream().filter(b -> b.getBossOrder() == 1).findFirst().get();
        NowBoss nowBoss = new NowBoss(null, null,
                nextBattleBoss.getName(), null, nextBattleBoss.getHpMax(),
                1, damagedBoss.getNowBoss().getBossRounds() + 1, stage);
        return new DamagedBoss(damagedBoss.getDamage(), nowBoss);
    }

    /**
     * 更新伤害信息
     *
     * @param damagedBoss
     * @param groupId
     * @param user
     * @param lost
     * @return
     */
    private void updateDamageInfo(DamagedBoss damagedBoss, String groupId, BattleUser user, double lost) {
        nowBossMapper.updateById(damagedBoss.getNowBoss());
        double damageTimes = user.component4() - lost;
        user.setDamageTimes(damageTimes);
        BattleDamage battleDamage = new BattleDamage(groupId, user.getQqId(),
                Objects.requireNonNull(damagedBoss.getNowBoss().getBossId()),
                damagedBoss.getDamage(), Objects.requireNonNull(damagedBoss.getNowBoss().getNowId()));
        battleDamageMapper.insert(battleDamage);
    }

    /**
     * 获取受伤boss
     *
     * @param originalMessage
     * @param groupId
     * @return
     * @throws Exception
     */
    private DamagedBoss getDamagedBoss(List<MessageChainPo.MessageInfo> originalMessage, String groupId) throws Exception {
        int damage = 0;
        NowBoss nowBoss;
        String base_damage_str = Objects.requireNonNull(StrUtil.removePrefix(originalMessage.get(0).getContent(), "报刀")).trim();
        if (base_damage_str.contains(" ")) {
            // 说明报刀规则是报刀x xxxxxx
            String[] var0 = base_damage_str.split(" ", 2);
            int boss_order = Integer.parseInt(var0[0]);
            damage = Integer.parseInt(var0[1]);
            nowBoss = nowBossMapper.selectNowBossWithOrder(groupId, boss_order);
        } else {
            // 查询当前boss
            // TODO: 2021/6/7 后续考虑redis或其他方式缓存？
            damage = Integer.parseInt(base_damage_str);
            nowBoss = nowBossMapper.selectByMinBossOrder(groupId);
        }
        if (nowBoss == null) {
            throw new Exception("nowBoss为空");
        }
        return new DamagedBoss(damage, nowBoss);
    }

    /**
     * 获取这是谁的刀
     *
     * @param originalMessage messageJson
     * @param senderId        发送者id
     * @param groupId         群id
     * @return
     * @throws Exception
     */
    private BattleUser getBattleUser(List<MessageChainPo.MessageInfo> originalMessage, String senderId, String groupId) throws Exception {
        MessageChainPo.MessageInfo messageInfo = originalMessage.stream()
                .filter(m -> m.getType().equals(At.SERIAL_NAME)).findFirst().orElse(null);
        // messageInfo==null说明是自己的刀
        String qqId = messageInfo == null ? senderId : messageInfo.getTarget();
        if (qqId == null) {
            throw new Exception("qqId为空");
        }
        // 查出来本人信息
        QueryWrapper<BattleUser> battleUserWrapper = new QueryWrapper<>();
        battleUserWrapper.eq("qq_id", qqId);
        battleUserWrapper.eq("group_id", groupId);
        BattleUser battleUser = battleUserMapper.selectOne(battleUserWrapper);
        if (battleUser == null) {
            throw new Exception("battleUser为空");
        }
        return battleUser;
    }


}
