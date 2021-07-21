package com.erisu.cloud.megumi.song.pojo

data class BasicSongResponse(
    val result: MusicList,
    val code: Int,
) {
    data class MusicList(
        val songs: List<Song>,
        val songCount: Int,
    ) {

    }
}
