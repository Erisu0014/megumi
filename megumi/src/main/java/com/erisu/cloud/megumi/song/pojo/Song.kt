package com.erisu.cloud.megumi.song.pojo

data class Song(
    val name: String,
    val id: Int,
    val ar: List<Artist>,
    val al: Album,
    val lyrics: List<String>?,
) {
    data class Artist(
        val id: Int,
        val name: String,
        val alias: Array<String>,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Artist

            if (id != other.id) return false
            if (name != other.name) return false
            if (!alias.contentEquals(other.alias)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + name.hashCode()
            result = 31 * result + alias.contentHashCode()
            return result
        }
    }

    data class Album(
        val id: Int,
        val name: String,
        val picUrl: String,
    )
}
