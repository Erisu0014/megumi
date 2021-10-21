package com.erisu.cloud.megumi.rss.pojo

data class UmaNews(
    val announce_id: Int,
    val title: String,
    val message: String,
    val post_at: String,
    val update_at: String?,
    val announce_label: Int,
    val image: String,
    val post_platform_flag: Int,
)
