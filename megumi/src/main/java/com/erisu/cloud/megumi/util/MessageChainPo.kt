package com.erisu.cloud.megumi.util

data class MessageChainPo(
    val type: String,
    val kind: String,
    val botId: String,
    val ids: List<String>,
    val fromId: String,
    val originalMessage: List<MessageInfo>
) {
    data class MessageInfo(
        val type: String,
        val content: String? = null,
        val target: String? = null
        //  后续字段待补充
    )
}

