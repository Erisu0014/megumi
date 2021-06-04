package com.erisu.cloud.megumi.battle.pojo

/**
 *@Description 会战boss
 *@Author Alice
 *@Date 2021/6/4 13:37
 **/
data class BattleBoss(
    var id: String? = null,
    val name: String? = null,
    val hpMax: Int? = null,
    val bossOrder: Int? = null
)
