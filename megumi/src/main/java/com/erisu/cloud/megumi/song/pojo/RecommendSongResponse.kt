package com.erisu.cloud.megumi.song.pojo

/**
 *@Description 每日推荐
 *@Author alice
 *@Date 2021/7/21 16:45
 **/
data class RecommendSongResponse(
    val code: Int,
    val data: RecommendData?,
) {
    data class RecommendData(
        val dailySongs: List<Song>,
        val orderSongs: List<Song>?,
        val recommendReasons: List<Reason>?,
    ) {
        data class Reason(val songId: Int, val reason: String)
    }
}