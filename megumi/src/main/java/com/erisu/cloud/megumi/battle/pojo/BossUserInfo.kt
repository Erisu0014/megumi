package com.erisu.cloud.megumi.battle.pojo

import kotlinx.serialization.Serializable

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/1/31 15:24
 **/
@Serializable
data class BossUserInfo(
    val userId: Long,
    val time: String, //"17:30"
) {
}