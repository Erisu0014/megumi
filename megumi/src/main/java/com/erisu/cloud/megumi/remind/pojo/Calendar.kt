package com.erisu.cloud.megumi.remind.pojo

import kotlinx.serialization.Serializable

@Serializable
data class Calendar(
    val name: String,
    val start_time: String,
    val end_time: String,
    val type: String? = null,
)
