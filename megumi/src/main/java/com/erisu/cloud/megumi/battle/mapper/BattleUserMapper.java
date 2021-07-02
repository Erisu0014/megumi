package com.erisu.cloud.megumi.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.battle.pojo.BattleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BattleUserMapper extends BaseMapper<BattleUser> {
    int insertIgnore(@Param("battleUser") BattleUser battleUser);

    @Override
    int updateById(@Param("battleUser") BattleUser battleUser);

    int insertAllIgnore(@Param("battleUsers") List<BattleUser> battleUsers);

    int revertDamageTimes(@Param("groupId") String groupId, @Param("senderId") String senderId, @Param("lost") double lost);
}
