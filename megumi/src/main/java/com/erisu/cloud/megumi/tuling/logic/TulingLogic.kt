package com.erisu.cloud.megumi.tuling.logic

import cn.hutool.core.lang.UUID
import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.tuling.pojo.*
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 *@Description tuling相关
 *@Author alice
 *@Date 2021/7/23 15:13
 **/
@Component
class TulingLogic {
    @Value("\${tulingUrl}")
    public lateinit var tulingUrl: String

    @Value("\${apikey}")
    public lateinit var apikey: String

    fun onlineAnswering(text: String): Message? {
        val request =
            TulingRequest(0, Perception(Text(text), null, null, null),
                UserInfo(apikey, UUID.fastUUID().toString(true)))
        val response = HttpUtil.post(tulingUrl, JSON.toJSONString(request), 2000)
        val tulingResponse = JSONObject.parseObject(response, TulingResponse::class.java)
        return if (tulingResponse.results.isNotEmpty() && tulingResponse.results[0].resultType == "text") {
            val result = JSONObject.parseObject(tulingResponse.results[0].values.toString(), Text::class.java)
            PlainText(result.text)
        } else {
            null
        }
    }

}