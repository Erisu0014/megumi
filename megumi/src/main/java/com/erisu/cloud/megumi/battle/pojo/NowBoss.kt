package com.erisu.cloud.megumi.battle.pojo

/**
 *@Description 当前boss
 *@Author Alice
 *@Date 2021/6/4 13:37
 **/
data class NowBoss(
    val bossId: String? = null,
    val bossName: String? = null,
    val groupId: String? = null,
    val hpNow: Int? = null,
    val bossOrder: Int? = null,
    val bossRounds: Int? = null,
    val nowStage: Int? = null
)
