package com.erisu.cloud.megumi.setu.logic

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.setu.pojo.SetuRequest
import com.erisu.cloud.megumi.setu.pojo.SetuResponse
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 *@Description setu logic
 *@Author alice
 *@Date 2021/7/7 8:46
 **/
@Component
class SetuLogic {
    @Value("\${qq.username}")
    private val username: Long = 0

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
        group.sendMessage(PlainText("setu正在下载中，请稍等~"))
        val responseJson =
            HttpUtil.post("https://api.lolicon.app/setu/v2", JSON.toJSONString(setuRequest), 2000)
        val setuResponse = JSONObject.parseObject(responseJson, SetuResponse::class.java)
        if (setuResponse.error == "" && !setuResponse.data.isNullOrEmpty()) {
//            val imageList: MutableList<Image> = mutableListOf()
            if (isR18 == 0) {
                setuResponse.data.forEach {
                    val path = FileUtil.downloadHttpUrl(it.urls.original, "cache", null, null)
//                    if (path != null) imageList.add(StreamMessageUtil.generateImage(group, path.toFile(), true))
                    //单条发送
                    val text = "pid：${it.pid}\n标题：${it.title}\n作者：${it.author}\n原地址：${it.urls.original}"
                    if (path != null) {
                        group.sendMessage(forwardSetuMessage(PlainText(text),
                            StreamMessageUtil.generateImage(group, path.toFile(), true), group))
                    }
                }
//                messageChainOf(*imageList.toTypedArray())
            } else {
                setuResponse.data.forEach { group.sendMessage(it.urls.original.toString()) }
//                PlainText(setuResponse.data[0].urls.original.toString())
            }
        } else if (setuResponse.data.isNullOrEmpty()) {
            group.sendMessage("那是什么色图？")
        } else {
            group.sendMessage("色图下载失败")
        }
        return null
    }

    fun forwardSetuMessage(text: PlainText, image: Image, contact: Contact): Message {
        return buildForwardMessage(contact) {
            add(2854196306, "色图bot", messageChainOf(text, image))
        }
    }
}