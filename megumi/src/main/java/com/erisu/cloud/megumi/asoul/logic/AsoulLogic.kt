package com.erisu.cloud.megumi.asoul.logic

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.asoul.pojo.AsoulCheckRequest
import com.erisu.cloud.megumi.asoul.pojo.AsoulCheckResponse
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import org.springframework.stereotype.Component

/**
 *@Description asoul logic
 *@Author alice
 *@Date 2021/8/27 14:42
 **/
@Component
class AsoulLogic {

    private val url = "https://asoulcnki.asia/v1/api/check"

    fun doubleCheck(text: String): Message? {
        if (text.length < 10 || text.length > 1000) return null
        val rate: Double
        val reply_url: String
        val content: String

        val response = HttpUtil.post(url, JSONObject.toJSONString(AsoulCheckRequest(text)))
        val asoulCheckResponse = JSON.parseObject(response, AsoulCheckResponse::class.java)
        if (asoulCheckResponse.code != 0 || asoulCheckResponse.data == null) return null
        rate = asoulCheckResponse.data.rate
        reply_url = asoulCheckResponse.data.related[0].reply_url
        content = asoulCheckResponse.data.related[0].reply.content
        return messageChainOf(PlainText("查重率:${rate}\n原文链接:${reply_url}\n" +
                "- - - - - -\n${content}"))
    }
}