package com.erisu.cloud.megumi.tuling.pojo

import kotlinx.serialization.Serializable

@Serializable
data class EmotionResponse(
    val text: String,
    val items: MutableList<Emotion>,
    val log_id: Long,
) {
    @Serializable
    data class Emotion(
        val prob: Double,
        val label: String,
        val subitems: MutableList<Emotion>? = null,
        val replies: MutableList<String>? = null,
    )
}
