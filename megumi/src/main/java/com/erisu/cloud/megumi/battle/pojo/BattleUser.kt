package com.erisu.cloud.megumi.battle.pojo

import com.baomidou.mybatisplus.annotation.TableId

data class BattleUser(
    @TableId
    var qqId: String,
    var nickname: String,
    var groupId: String,
    var damageTimes: Double = 0.0
)
