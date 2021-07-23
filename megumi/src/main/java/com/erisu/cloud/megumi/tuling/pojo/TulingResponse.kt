package com.erisu.cloud.megumi.tuling.pojo

data class TulingResponse(
    val intent: Intent,
    val results: List<Result>,
) {
    data class Intent(
        val code: Int,
        val intentName: String,
        val actionName: String,
        val parameters: Map<String, String>?,
    )

    data class Result(
        val groupType: Int,
        val resultType: String,
        val values: Any,
    )
}
