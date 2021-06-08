package com.erisu.cloud.megumi.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.battle.pojo.BattleBoss;
import com.erisu.cloud.megumi.battle.pojo.NowBoss;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NowBossMapper extends BaseMapper<NowBoss> {
    /**
     * 批量插入boss，one round
     *
     * @param bosses     boss数组
     * @param groupId    群组id
     * @param bossRounds boss周目
     * @param nowStage   boss阶段
     */
    int insertStageBoss(@Param("bosses") List<BattleBoss> bosses,
                        @Param("groupId") String groupId, @Param("bossRounds") int bossRounds, @Param("nowStage") int nowStage);

    // TODO: 2021/6/7 修改damage表，增加now_id，用以撤销报刀 
    NowBoss selectByMinBossOrder(@Param("groupId") String groupId);

    List<NowBoss> selectNowBoss(@Param("groupId") long id);
}
