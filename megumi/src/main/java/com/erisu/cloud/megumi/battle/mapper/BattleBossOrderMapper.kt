package com.erisu.cloud.megumi.battle.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.erisu.cloud.megumi.battle.pojo.BattleBossOrder
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 *@Description boss order相关
 *@Author alice
 *@Date 2021/6/28 16:18
 **/
@Mapper
interface BattleBossOrderMapper : BaseMapper<BattleBossOrder> {
    fun insertIgnore(@Param("battleBossOrder") battleBossOrder: BattleBossOrder?): Int
}