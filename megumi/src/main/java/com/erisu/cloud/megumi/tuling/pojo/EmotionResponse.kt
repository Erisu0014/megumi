package com.erisu.cloud.megumi.tuling.pojo

import kotlinx.serialization.Serializable

@Serializable
data class EmotionResponse(
    val text: String?=null,
    val items: MutableList<Emotion>?=null,
    val log_id: Long?=null,
    val error_code:Int?=null,
    val error_msg:String?=null
) {
    @Serializable
    data class Emotion(
        val prob: Double,
        val label: String,
        val subitems: MutableList<Emotion>? = null,
        val replies: MutableList<String>? = null,
    )
}
