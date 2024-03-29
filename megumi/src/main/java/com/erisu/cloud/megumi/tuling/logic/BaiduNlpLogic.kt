package com.erisu.cloud.megumi.tuling.logic

import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.tuling.pojo.CheckSignResponse
import com.erisu.cloud.megumi.tuling.pojo.Emoticon
import com.erisu.cloud.megumi.tuling.pojo.EmotionResponse
import com.erisu.cloud.megumi.util.StreamMessageUtil
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Message
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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
        val checkSignResponse: CheckSignResponse = JSONObject.parseObject(response.body!!.string(),CheckSignResponse::class.java)
        accessToken = checkSignResponse.access_token!!
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
            if (emotionResponse.error_code!=null){
                return null
            }
            val emotionCategory = emotionResponse.items!!.sortedByDescending { it.prob }[0]
//            if (emotionCategory.subitems != null && emotionCategory.subitems.isNotEmpty()) {
//                emotionCategory.subitems.sortedByDescending { it.prob }[0].label
//            } else {
//                emotionCategory.label
//            }
            return when (emotionCategory.label) {
                Emoticon.like.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/纳西妲拍手.gif").inputStream)
                }
                Emoticon.thankful.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/感谢.jpg").inputStream)
                }
                Emoticon.optimistic.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/露娜笑.jpg").inputStream)
                }
                Emoticon.neutral.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/白猫.gif").inputStream)
                }
                Emoticon.pessimistic.name,Emoticon.complaining.name,Emoticon.angry.name,
                Emoticon.disgusting.name,Emoticon.fearful.name,Emoticon.sad.name -> {
                    StreamMessageUtil.generateImage(group,
                        ClassPathResource("emoticon/喜多哭.gif").inputStream)
                }

                else -> null
            }
        }
        return null
    }
}