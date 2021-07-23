package com.erisu.cloud.megumi.setu.logic

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.setu.pojo.SetuRequest
import com.erisu.cloud.megumi.setu.pojo.SetuResponse
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.MessageUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import org.springframework.stereotype.Component

/**
 *@Description setu logic
 *@Author alice
 *@Date 2021/7/7 8:46
 **/
@Component
class SetuLogic {
    suspend fun getRollSetu(tag: String, num: Int, isR18: Int, group: Group): Message? {
        val orTag = tag.split("|")
        val setuRequest: SetuRequest = if (tag.trim() == "") {
            SetuRequest(dateAfter = null,
                dateBefore = null,
                uid = null,
                tag = null,
                keyword = null,
                num = num,
                r18 = isR18)
        } else {
            SetuRequest(dateAfter = null,
                dateBefore = null,
                uid = null,
                tag = arrayOf(orTag),
                keyword = null,
                num = num, r18 = isR18)
        }
        val responseJson =
            HttpUtil.post("https://api.lolicon.app/setu/v2", JSON.toJSONString(setuRequest),2000)
        val setuResponse = JSONObject.parseObject(responseJson, SetuResponse::class.java)
        return if (setuResponse.error == "") {
            val imageList: MutableList<Image> = mutableListOf()
            if (isR18 == 0) {
                setuResponse.data.forEach {
                    val path = FileUtil.downloadHttpUrl(it.urls.original, "cache", null, null)
                    if (path != null) imageList.add(MessageUtil.generateImage(group, path.toFile(), true))
                }
                messageChainOf(*imageList.toTypedArray())
            } else {
                PlainText(setuResponse.data[0].urls.original.toString())
            }
        } else null
    }
}