package com.erisu.cloud.megumi.battle.pojo

import com.baomidou.mybatisplus.annotation.TableId

data class BattleGroup(
    @TableId
    var groupId: String? = null,
    val groupName: String? = null
)
