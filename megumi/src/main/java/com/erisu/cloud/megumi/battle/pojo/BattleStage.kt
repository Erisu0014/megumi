package com.erisu.cloud.megumi.battle.pojo

import com.baomidou.mybatisplus.annotation.TableId

/**
 * @Description 每阶段的5个boss
 * @Author alice
 * @Date 2021/6/4 13:37
 **/
data class BattleStage(
    @TableId
    var id: Int? = null,
    val boss1Id: String? = null,
    val boss2Id: String? = null,
    val boss3Id: String? = null,
    val boss4Id: String? = null,
    val boss5Id: String? = null
)
