package com.erisu.cloud.megumi.asoul.pojo

data class AsoulCheckResponse(
    val code: Int,
    val message: String,
    val data: RelatesInfo?,

    ) {
    data class RelatesInfo(
        val rate: Double,
        val start_time: Int,
        val end_time: Int,
        val related: List<Relates>,
    ) {
        data class Relates(
            val rate: Double,
            val reply_url: String,
            val reply: Reply,
        ) {
            data class Reply(
                val rpid: String,
                val type_id: Int,
                val dynamic_id: String,
                val mid: Int,
                val uid: Int,
                val oid: String,
                val ctime: Int,
                val m_name: String,
                val content: String,
                val like_num: Int,
                val origin_rpid: String,
                val similar_count: Int,
                val similar_like_sum: String,
            )
        }
    }
}
