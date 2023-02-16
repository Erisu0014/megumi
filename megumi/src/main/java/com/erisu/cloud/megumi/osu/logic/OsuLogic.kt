package com.erisu.cloud.megumi.osu.logic

import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.osu.pojo.BeatMapInfo
import com.erisu.cloud.megumi.tuling.pojo.CheckSignResponse
import com.erisu.cloud.megumi.util.FileUtil
import okhttp3.*
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/2/16 9:22
 **/
@Component
class OsuLogic {
    companion object {
        const val baseUrl = "https://osu.ppy.sh/api/v2"
        var token = {
            val path = "${FileUtil.localStaticPath}${File.separator}osu${File.separator}token.txt"
            File(path).readLines().joinToString(separator = "")
        }
    }

    fun getMapInfo(beatmapId: String): BeatMapInfo? {
        val url = "${baseUrl}/beatmaps/${beatmapId}"
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        var beatMapInfo: BeatMapInfo? = null
        val response = client.newCall(
            Request.Builder().header("Authorization", "Bearer ${token()}").url(url).build()
        ).execute()
        if (!response.isSuccessful) {
            throw Exception("response返回异常，错误码:${response.code}")
        }
        beatMapInfo = JSONObject.parseObject(response.body!!.string(), BeatMapInfo::class.java)
        return beatMapInfo
    }
}