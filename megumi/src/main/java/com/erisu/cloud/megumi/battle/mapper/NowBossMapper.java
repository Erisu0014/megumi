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

    /**
     * 查询本轮最小序未死亡boss
     *
     * @param groupId
     */
    NowBoss selectByMinBossOrder(@Param("groupId") String groupId);

    /**
     * 查询本轮所有boss
     *
     * @param groupId
     */
    List<NowBoss> selectNowBoss(@Param("groupId") long groupId);

    NowBoss selectNowBossWithOrder(@Param("groupId") String id, @Param("bossOrder") int bossOrder);

    int revertDamage(@Param("nowBossId") int nowBossId, @Param("damage") int damage);
}
