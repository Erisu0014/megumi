package com.erisu.cloud.megumi.tuling.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.message.MessageUtil
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.tuling.logic.TulingLogic
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.random.Random

/**
 *@Description tuling测试
 *@Author alice
 *@Date 2021/7/23 15:42
 **/
@Component
@Model(name = "tuling")
class TulingService {
    companion object {
        private var probability = 20
    }

    @Resource
    private lateinit var tulingLogic: TulingLogic

    @Resource
    private lateinit var messageUtil: MessageUtil

    @Command(commandType = CommandType.GROUP, value = "", pattern = Pattern.CONTAINS)
    fun onlineAnswering(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val contentToString = messageChain.contentToString()
        if (contentToString.startsWith("调整AI概率") || contentToString.startsWith("调整百度AI")) {
            return null
        }
        if (Random.nextInt(100) < probability) {
            val answer = tulingLogic.onlineAnswering(contentToString)
            messageUtil.sendAsyncMessage(subject as Group?, answer, 5)
        }
        return null
    }

    @Command(commandType = CommandType.GROUP, value = "调整AI概率", pattern = Pattern.PREFIX)
    @Throws(Exception::class)
    fun changeProbability(sender: User, messageChain: MessageChain, subject: Contact): Message {
        probability = messageChain.contentToString().removePrefix("调整AI概率").trim().toInt()
        probability = if (probability > 100) 100 else probability
        return PlainText("当前AI概率为$probability")
    }


}