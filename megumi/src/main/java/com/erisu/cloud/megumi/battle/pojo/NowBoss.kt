package com.erisu.cloud.megumi.battle.pojo

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

/**
 *@Description 当前boss,对于now_boss需要考虑撤销刀的情况，无法直接删除
 *@Author Alice
 *@Date 2021/6/4 13:37
 **/
data class NowBoss(
    @TableId(type = IdType.AUTO)
    var nowId: Int?=null, // 为什么有null，因为涉及重新分配
    var bossId: String?=null,
    var bossName: String,
    var groupId: String?=null,
    var hpNow: Int = 0,
    var bossOrder: Int,
    var bossRounds: Int,
    var nowStage: Int
) {
}

