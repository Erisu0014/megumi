package com.erisu.cloud.megumi.battle.pojo

/**
 *@Description 会战boss
 *@Author Alice
 *@Date 2021/6/4 13:37
 **/
data class BattleBoss(
    var id: String,
    val name: String,
    val hpMax: Int,
    val bossOrder: Int
)
