package com.erisu.cloud.megumi.tuling.pojo


data class TulingRequest(
    val reqType: Int,
    val perception: Perception,
    val userInfo: UserInfo,
)
