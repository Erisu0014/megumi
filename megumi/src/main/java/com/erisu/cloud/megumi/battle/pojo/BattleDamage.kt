package com.erisu.cloud.megumi.battle.pojo

data class BattleDamage(
    val groupId: String,
    val qqId: String,
    //  battleBoss中的bossId
    val BossId: String,
    val damage: Int,
    //  nowId，在nowBoss中是递增的
    val nowBossId:Int
)
