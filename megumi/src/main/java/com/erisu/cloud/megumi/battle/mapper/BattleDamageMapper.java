package com.erisu.cloud.megumi.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.battle.pojo.BattleDamage;
import org.apache.ibatis.annotations.Param;

/**
 * @Description BattleDamageMapper
 * @Author alice
 * @Date 2021/6/4 17:31
 **/
public interface BattleDamageMapper extends BaseMapper<BattleDamage> {
    BattleDamage selectLastDamage(@Param("groupId") String groupId, @Param("senderId") String senderId);
}
