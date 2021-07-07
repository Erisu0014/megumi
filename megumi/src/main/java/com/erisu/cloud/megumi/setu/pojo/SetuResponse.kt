package com.erisu.cloud.megumi.setu.pojo

data class SetuResponse constructor(
    var error: String,
    var data: MutableList<SetuPo> = mutableListOf(),
) {
    data class SetuPo(
        var pid: Int,
        var p: Int,
        var uid: Int,
        var title: String,
        var author: String,
        var r18: Int,
        var width: Int,
        var height: Int,
        var tags: Array<String>,
        var ext: String,
        var uploadDate: Long,
        var urls: SizeUrl,
    ) {
        data class SizeUrl(
            var original: String?,
            var regular: String?,
            var small: String?,
            var thumb: String?,
            var mini: String?,
        )
    }
}
