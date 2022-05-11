package com.erisu.cloud.megumi.bilibili.service

import com.erisu.cloud.megumi.bilibili.logic.BiliSearchLogic
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Description get help info
 * @Author alice
 * @Date 2021/10/11 10:41
 */
@Component
@Model(name = "bilibili", uuid = "49bd68a24eff469187d777131012404f", help =
"""
[查成分 用户名]：此处用户名为b站用户名，例如：单推爱丽丝
[查成分 @群友]：需要该群友绑定b站uid才可以查询
[查成分]：需要本人绑定b站uid才可以查询
除此以外，还有b站视频解析功能，支持：av号、bv号、小程序、b23.tv解析
""")
class BiliService {
    @Resource
    private lateinit var biliSearchLogic: BiliSearchLogic

    @Resource
    private lateinit var redisUtil: RedisUtil

    @Command(commandType = CommandType.GROUP,
        value = "查成分",
        pattern = Pattern.PREFIX,
        uuid = "ed386314c7124c619a0025934a0b7a1d")
    @Throws(Exception::class)
    fun searchVtb(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        var pair: Triple<String, String, String>? = null
        // 纯文本
        if (messageChain.size == 2) {
            var user = ""
            if (messageChain.contentToString() == "查成分") {
                val mid = redisUtil.get("${RedisKey.BILIBILI.key}:${sender.id}")
                if (mid == "") return PlainText("您还没有绑定uid哦~发送绑定B站 114514(本人uid)即可绑定")
                pair = biliSearchLogic.searchUser(mid.toInt())
            } else {
                user = messageChain.contentToString().removePrefix("查成分 ").trim()
                pair = biliSearchLogic.searchUser(user)
            }
            if (user.length > 40) return null
        } else if (messageChain.size >= 3 && messageChain[1].contentToString() == "查成分" && messageChain[2] is At) {
            val at = messageChain[2] as At
            val mid = redisUtil.get("${RedisKey.BILIBILI.key}:${at.target}")
                ?: return PlainText("他还没有绑定uid哦~发送绑定B站 114514(本人uid)即可绑定")
            pair = biliSearchLogic.searchUser(mid.toInt())
        }
        if (pair != null) {
            val follow = biliSearchLogic.searchFollow(pair.second)
            if (!follow.isNullOrEmpty()) {
                val vtbs = biliSearchLogic.checkDD(follow)
                return if (vtbs != null && vtbs.size != 0) {
                    messageChainOf(PlainText("${pair.first}关注的皮套人有${vtbs.size}位，他们是：\n${
                        vtbs.joinToString(separator = ",")
                    }"))
                } else {
                    messageChainOf(PlainText("${pair.first}没有关注皮套人，干的漂亮！"))
                }
            }
        } else {
            return PlainText("这位更是歌\uD83C\uDFA4")
        }
        return null
    }


    @Command(commandType = CommandType.GROUP,
        value = "绑定(b|B)站 [0-9]{3,8}",
        pattern = Pattern.REGEX)
    fun bindUid(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val mid = messageChain.contentToString().substring(5)
        redisUtil.set("${RedisKey.BILIBILI.key}:${sender.id}", mid)
        return PlainText("绑定成功喵")
    }

    @Command(commandType = CommandType.GROUP,
        value = "解绑(b|B)站",
        pattern = Pattern.REGEX)
    fun unbindUid(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val mid = messageChain.contentToString().substring(5)
        redisUtil.delete("${RedisKey.BILIBILI.key}:${sender.id}")
        return PlainText("解绑成功喵")
    }

    @Command(commandType = CommandType.GROUP,
        pattern = Pattern.REGEX,
        value = ".*?([Aa][Vv](\\d{1,12})|([Bb][Vv]1[A-Za-z0-9]{2}4.1.7[A-Za-z0-9]{2})|https://(b23.tv)/([A-Za-z0-9]{7})).*?")
    suspend fun pulipuli(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val finder =
            Regex(".*?([Aa][Vv](\\d{1,12})|([Bb][Vv]1[A-Za-z0-9]{2}4.1.7[A-Za-z0-9]{2})|https://(b23.tv)/([A-Za-z0-9]{7})).*?")
                .find(messageChain.contentToString()) ?: return null
        if (finder.groupValues[4] == "b23.tv") {
            val bvCode = biliSearchLogic.getOriginalLink(finder.groupValues[5]) ?: return null
            return biliSearchLogic.getAvData(subject as Group, "BV$bvCode")
        } else {
            val code = if (finder.groupValues[2] != "") finder.groupValues[2] else finder.groupValues[3]
            return biliSearchLogic.getAvData(subject as Group, code)

        }
    }


//    @Command(commandType = CommandType.GROUP,
//        pattern = Pattern.REGEX,
//        value = ".+?哔哩哔哩.+?https://b23.tv/(.+?)\\?.+")
//    suspend fun pulipuli2(sender: User, messageChain: MessageChain, subject: Contact): Message? {
//        val finder =
//            Regex(".+?哔哩哔哩.+?https://b23.tv/(.+?)\\?.+").find(
//                messageChain.contentToString())
//                ?: return null
//        val bvCode = biliSearchLogic.getOriginalLink(finder.groupValues[1]) ?: return null
//        return biliSearchLogic.getAvData(subject as Group, "BV$bvCode")
//    }

}