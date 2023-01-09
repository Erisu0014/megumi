package com.erisu.cloud.megumi.tuling.pojo

data class TulingResponse(
    val emotion:Emotion,
    val intent: Intent,
    val results: List<Result>,
) {
    data class Emotion(
        val robotEmotion: InnerEmotion,
        val userEmotion: InnerEmotion
        ){
        data class InnerEmotion(
            val a:Int,
            val d:Int,
            val emotionId:Int,
            val p:Int
        )
    }
    data class Intent(
        val code: Int,
        val appKey:String,
        val operateState:Int
    )

    data class Result(
        val groupType: Int,
        val resultType: String,
        val values: Text,
    )
}
