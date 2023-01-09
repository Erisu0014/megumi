package com.erisu.cloud.megumi.tuling.logic

import com.erisu.cloud.megumi.tuling.pojo.CheckSignResponse
import com.erisu.cloud.megumi.tuling.pojo.Emoticon
import com.erisu.cloud.megumi.tuling.pojo.EmotionResponse
import com.erisu.cloud.megumi.util.StreamMessageUtil
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.internal.deps.okhttp3.MediaType
import net.mamoe.mirai.internal.deps.okhttp3.MediaType.Companion.toMediaTypeOrNull
import net.mamoe.mirai.internal.deps.okhttp3.OkHttpClient
import net.mamoe.mirai.internal.deps.okhttp3.Request
import net.mamoe.mirai.internal.deps.okhttp3.RequestBody
import net.mamoe.mirai.internal.deps.okhttp3.RequestBody.Companion.toRequestBody
import net.mamoe.mirai.message.data.Message
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 *@Description 百度nlp相关
 *@Author alice
 *@Date 2021/11/4 10:40
 **/
@ExperimentalSerializationApi
@Component
class BaiduNlpLogic {
    @Value("\${baidu.client_id}")
    private lateinit var clientId: String

    @Value("\${baidu.client_secret}")
    private lateinit var clientSecret: String

    companion object {
        private var accessToken: String = ""
    }


    @Throws(Exception::class)
    fun getAccessToken() {
        val url =
            "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=${clientId}&client_secret=${clientSecret}"
        val client =
            OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build()
        val response = client.newCall(Request.Builder().url(url).build()).execute()
        if (!response.isSuccessful) {
            throw Exception("response返回异常，错误码:${response.code}")
        }
        val checkSignResponse: CheckSignResponse = Json.decodeFromString(response.body!!.string())
        accessToken = checkSignResponse.access_token
    }

    @Throws(Exception::class)
    suspend fun emotionRecognition(group: Group, text: String): Message? {
        getAccessToken()
        val url =
            "https://aip.baidubce.com/rpc/2.0/nlp/v1/emotion?access_token=${accessToken}"
        val requestBody = """{"text":"$text"}""".toRequestBody("application/json".toMediaTypeOrNull())
        val client =
            OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build()
        val response = client.newCall(Request.Builder().url(url).post(requestBody).build()).execute()
        if (response.isSuccessful) {
            val emotionResponse: EmotionResponse = Json.decodeFromString(response.body!!.string())
            val emotionCategory = emotionResponse.items.sortedByDescending { it.prob }[0]
//            if (emotionCategory.subitems != null && emotionCategory.subitems.isNotEmpty()) {
//                emotionCategory.subitems.sortedByDescending { it.prob }[0].label
//            } else {
//                emotionCategory.label
//            }
            return when (emotionCategory.label) {
                Emoticon.optimistic.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/麦昆高兴.jpg").inputStream)
                }
                Emoticon.neutral.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/麦昆得意.jpg").inputStream)
                }
                Emoticon.pessimistic.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/飞鹰哭了.jpg").inputStream)
                }
                else -> null
            }
        }
        return null
    }
}