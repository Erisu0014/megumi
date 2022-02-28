package com.erisu.cloud.megumi.tekoki.logic

import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.tekoki.pojo.Tekoki
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import org.springframework.stereotype.Component
import java.io.File
import kotlin.random.Random

/**
 *@Description tekoki logic
 *@Author alice
 *@Date 2021/9/16 9:24
 **/
@Component
class TekokiLogic {
    private val basicPath = "${FileUtil.localStaticPath}${File.separator}tekoki"
    suspend fun tekoki(sender: User, group: Group): Message? {
        return null
//        val file =
//            File("${basicPath}${File.separator}tekoki.json")
//        val jsonStr = file.readLines().joinToString(separator = "") { it }
//        val tekoki = JSONObject.parseObject(jsonStr, Tekoki::class.java)
//        val food = tekoki.foods[Random.nextInt(0, tekoki.foods.size)]
//        val foodFile = File("${basicPath}${File.separator}food${File.separator}${food.pic}")
//        val image = StreamMessageUtil.generateImage(group, foodFile, false)
//        return messageChainOf(At(sender.id), PlainText("今天冲${food.name}(${food.website})吧\n${food.url}"), image)
    }
}