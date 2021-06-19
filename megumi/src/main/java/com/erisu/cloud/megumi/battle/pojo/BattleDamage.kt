package com.erisu.cloud.megumi.battle.pojo

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

data class BattleDamage(
    @TableId(type = IdType.AUTO)
    val id: Int?,
    val groupId: String,
    val qqId: String,
    //  battleBoss中的bossId
    val BossId: String,
    val damage: Int,
    //  nowId，在nowBoss中是递增的
    val nowBossId: Int
)
