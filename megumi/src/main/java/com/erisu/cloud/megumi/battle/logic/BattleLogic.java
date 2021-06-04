package com.erisu.cloud.megumi.battle.logic;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erisu.cloud.megumi.battle.mapper.BattleBossMapper;
import com.erisu.cloud.megumi.battle.mapper.BattleGroupMapper;
import com.erisu.cloud.megumi.battle.mapper.BattleStageMapper;
import com.erisu.cloud.megumi.battle.mapper.NowBossMapper;
import com.erisu.cloud.megumi.battle.pojo.BattleBoss;
import com.erisu.cloud.megumi.battle.pojo.BattleGroup;
import com.erisu.cloud.megumi.battle.pojo.BattleStage;
import com.erisu.cloud.megumi.battle.pojo.NowBoss;
import com.erisu.cloud.megumi.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.sun.org.apache.xpath.internal.operations.Bool;
import kotlinx.serialization.json.Json;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private NowBossMapper nowBossMapper;

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
        QueryWrapper<NowBoss> wrapper = new QueryWrapper<>();
        wrapper.eq(true, "group_id", String.valueOf(id));
        return nowBossMapper.selectList(wrapper);
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


}
