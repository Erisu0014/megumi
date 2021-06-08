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
import com.erisu.cloud.megumi.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.util.MessageChainPo;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
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
    public String fuckBoss(User sender, MessageChain messageChain, Group group, Boolean checkCompleted) {
        /*
         1.判断是否为自己的刀
         2.判断打的是什么boss
         3.判断是否尾刀
         4.判断是否有刀
         5.判断是否当前轮次结束，注入新一轮boss
         6.尾刀改变预约状态
         */
        String fuckResult;
        String messageJsonString = MessageChain.serializeToJsonString(messageChain);
        List<Object> messageObjects = JSON.parseArray(messageJsonString, Object.class);

        if (!CollUtil.isNotEmpty(messageObjects) || messageObjects.get(0) == null) {
            return "唔，出问题了，联系爱丽丝姐姐看看吧";
        }
        MessageChainPo messageChainPo = JSONObject.parseObject(JSON.toJSONString(messageObjects.get(0)), MessageChainPo.class);
        List<MessageChainPo.MessageInfo> originalMessage = messageChainPo.getOriginalMessage();
        // 1.判断是否为自己的刀
        MessageChainPo.MessageInfo messageInfo = originalMessage.stream()
                .filter(m -> m.getType().equals(At.SERIAL_NAME)).findFirst().orElse(null);
        String qqId;
        if (messageInfo == null) {
            // 说明是自己的刀
            qqId = String.valueOf(sender.getId());
        } else {
            // 说明是别人的刀
            qqId = messageInfo.getTarget();
        }
        if (qqId == null) {
            return "唔，出问题了，联系爱丽丝姐姐看看吧";
        }
        // 查出来本人信息
        QueryWrapper<BattleUser> battleUserWrapper = new QueryWrapper<>();
        battleUserWrapper.eq("qq_id", qqId);
        battleUserWrapper.eq("group_id", group.getId());
        BattleUser battleUser = battleUserMapper.selectOne(battleUserWrapper);
        // 2.判断是几号boss
        int damage;
        int boss_order;
        NowBoss nowBoss;
        String base_damage_str = Objects.requireNonNull(StrUtil.removePrefix(originalMessage.get(0).getContent(), "报刀")).trim();
        if (base_damage_str.contains(" ")) {
            // 说明报刀规则是报刀x xxxxxx
            String[] var0 = base_damage_str.split(" ", 2);
            boss_order = Integer.parseInt(var0[0]);
            damage = Integer.parseInt(var0[1]);
            QueryWrapper<NowBoss> nowBossWrapper = new QueryWrapper<>();
            nowBossWrapper.eq("boss_order", boss_order);
            nowBossWrapper.eq("group_id", group.getId());
            nowBoss = nowBossMapper.selectOne(nowBossWrapper);
        } else {
            // 查询当前boss
            // TODO: 2021/6/7 后续考虑redis或其他方式缓存？
            damage = Integer.parseInt(base_damage_str);
            nowBoss = nowBossMapper.selectByMinBossOrder(String.valueOf(group.getId()));
            boss_order = nowBoss.getBossOrder();
        }
        if (nowBoss == null) {
            return "唔，出问题了，联系爱丽丝姐姐看看吧";
        }
        // 2.判断是否尾刀
        double lost;
        boolean isLast;
        DamageType damageType;
        if (nowBoss.getHpNow() <= damage) {
            // 说明这要出尾刀
            damage = nowBoss.getHpNow();//对damage重新赋值
            lost = 0.5;
            damageType = DamageType.Last;
            isLast = true;

        } else {
            // 说明打不死，但不表示不是补偿刀
            if (Math.floor(nowBoss.getHpNow()) == nowBoss.getHpNow()) {
                lost = 1;
                damageType = DamageType.Complete;
            } else {
                lost = 0.5;
                damageType = DamageType.Incomplete;
            }

            isLast = false;
        }
        //  没刀了
        if (battleUser.getDamageTimes() - lost <= 0) {
            if (!battleUser.getQqId().equals(String.valueOf(sender.getId()))) {
                return "你再想想！" + battleUser.getNickname() + "已经出完三刀下班了哟~";
            } else {
                return "你再想想！你已经出完三刀下班了哟~";
            }
        }
        // 判断为最后一刀
        if (isLast) {
            nowBoss.setHpNow(0);
            nowBossMapper.updateById(nowBoss);
            double damageTimes = battleUser.component4() - lost;
            battleUser.setDamageTimes(damageTimes);
            BattleDamage battleDamage = new BattleDamage(String.valueOf(group.getId()), qqId, nowBoss.getBossId(), damage, nowBoss.getNowId());
            battleDamageMapper.insert(battleDamage);

            // TODO: 2021/6/8 提醒预约和挂树的
            //判断是否为最后一个boss，需要注入新一轮boss
            if (boss_order == 5) {
                int stage = BattleFormat.INSTANCE.getStage(nowBoss.getNowStage() + 1);
                List<BattleBoss> battleBosses = searchStageBoss(stage);
                nowBossMapper.insertStageBoss(battleBosses, String.valueOf(group.getId()), nowBoss.getBossRounds() + 1, stage);
                BattleBoss battleBoss = battleBosses.stream().filter(b -> b.getBossOrder() == 1).findFirst().get();
                fuckResult = BattleFormat.INSTANCE.fuckBossLastInfo(damageType, battleUser.getNickname(), damage, battleBoss.getBossOrder(), nowBoss.getBossRounds() + 1, battleBoss.getHpMax(), damageTimes);
            } else {
                QueryWrapper<NowBoss> nowBossQueryWrapper = new QueryWrapper<>();
                nowBossQueryWrapper.eq("group_id", group.getId());
                nowBossQueryWrapper.eq("boss_order", nowBoss.getBossOrder() + 1);
                nowBossQueryWrapper.eq("boss_rounds", nowBoss.getBossRounds());
                NowBoss nowBossNext = nowBossMapper.selectOne(nowBossQueryWrapper);
                if (nowBossNext == null) {
                    return "唔，出问题了，联系爱丽丝姐姐看看吧";
                }
                fuckResult = BattleFormat.INSTANCE.fuckBossLastInfo(damageType, battleUser.getNickname(), damage, nowBossNext.getBossOrder(), nowBossNext.getBossRounds(), nowBossNext.getHpNow(), damageTimes);
            }
        } else {
            double damageTimes = battleUser.component4() - lost;
            nowBoss.setHpNow(nowBoss.getHpNow() - damage);
            nowBossMapper.updateById(nowBoss);
            battleUser.setDamageTimes(damageTimes);
            BattleDamage battleDamage = new BattleDamage(String.valueOf(group.getId()), qqId, nowBoss.getBossId(), damage, nowBoss.getNowId());
            battleDamageMapper.insert(battleDamage);
            fuckResult = BattleFormat.INSTANCE.fuckBossInfo(damageType, battleUser.getNickname(), damage, nowBoss.getBossOrder(), nowBoss.getBossRounds(), nowBoss.getHpNow(), damageTimes);
        }
        battleUserMapper.updateById(battleUser);
        return fuckResult;

    }


}
