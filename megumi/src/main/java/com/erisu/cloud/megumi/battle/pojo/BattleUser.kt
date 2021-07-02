package com.erisu.cloud.megumi.battle.pojo

data class BattleUser(
    var qqId: String,
    var nickname: String,
    var groupId: String,
    var damageTimes: Double = 0.0
)
