package com.erisu.cloud.megumi.osu

import cn.hutool.http.HttpUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object BaseConfig {
    const val baseUrl = "https://osu.ppy.sh/api/v2/"
    private const val clientInfo = """
        {
  "client_id": "",
  "client_secret": "",
  "code": "",
  "grant_type": "",
  "redirect_uri": ""
}
    """

    fun getToken(): TokenResponse {
        val tokenUrl = "https://osu.ppy.sh/oauth/token"
        val response = HttpUtil.post(tokenUrl, clientInfo, 3000)
        return Json.decodeFromString(response)
    }
}

fun main() {
    println(BaseConfig.getToken())
}