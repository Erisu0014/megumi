package com.erisu.cloud.megumi.setu.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.random.logic.RandomLogic.roll
import com.erisu.cloud.megumi.setu.logic.SetuLogic
import com.erisu.cloud.megumi.util.FileUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.Resource

/**
 *@Description setu插件
 *@Author alice
 *@Date 2021/7/7 8:43
 **/
@MiraiExperimentalApi
@Component
@Model(name = "setu",help =
"""
时代的眼泪
""")
class SetuService {
    @Resource
    lateinit var setuLogic: SetuLogic

    @Command(value = "来[点张](.*)色图", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    suspend fun roll(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val find = Regex("来[点张](.*)色图").find(messageChain.contentToString())!!.groupValues
        val tag: String = find[1]
        return if (tag.startsWith("r18")) {
            setuLogic.getRollSetu(tag.removePrefix("r18"), 1, 1, subject as Group)
        } else {
            setuLogic.getRollSetu(tag, 1, 0, subject as Group)
        }
    }

    @Command(value = "来([0-9]+)张(.*)色图", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    suspend fun rollWithNum(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val find = Regex("来([0-9]+)张(.*)色图").find(messageChain.contentToString())!!.groupValues
        val num: String = find[1]
        if (num.toInt() >= 5) {
            return PlainText("冲太多对身体不好ヽ(*。>Д<)o゜")
        }
        val tag: String = find[2]
        return if (tag.startsWith("r18")) {
            setuLogic.getRollSetu(tag.removePrefix("r18"), num.toInt(), 1, subject as Group)
        } else {
            setuLogic.getRollSetu(tag, num.toInt(), 0, subject as Group)
        }
    }


//    @Command(
//        value = "就这不够色",
//        commandType = CommandType.GROUP,
//        pattern = Pattern.EQUALS,
//        recallTime = 60,
//        isRecalled = true
//    )
//    suspend fun roll18(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        val path = "${FileUtil.localStaticPath}${File.separator}eroi${File.separator}pic${File.separator}18+"
//        return setuLogic.getLocalSetu(subject as Group, path, 1)
//
//    }

//    @Command(
//        value = "来张([色涩])图",
//        commandType = CommandType.GROUP,
//        pattern = Pattern.REGEX
//    )
//    suspend fun roll(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        val path = "${FileUtil.localStaticPath}${File.separator}eroi${File.separator}pic${File.separator}18-"
//        return setuLogic.getLocalSetu(subject as Group, path, 1)
//    }
//
//    @Command(
//        value = "来组([色涩])图",
//        commandType = CommandType.GROUP,
//        pattern = Pattern.REGEX
//    )
//    suspend fun rollMax(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        Regex("来组([色涩])图")
//        val path = "${FileUtil.localStaticPath}${File.separator}eroi${File.separator}pic${File.separator}18-"
//        return setuLogic.getLocalSetu(subject as Group, path, 5)
//    }


}