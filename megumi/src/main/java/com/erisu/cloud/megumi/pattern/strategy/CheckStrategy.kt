package com.erisu.cloud.megumi.pattern.strategy

import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.pattern.PatternStrategy
import com.erisu.cloud.megumi.pattern.PatternSupport
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component

/**
 *@Description 检测消息是否为纯文本
 *@Author alice
 *@Date 2021/10/20 10:06
 **/
@PatternSupport(pattern = Pattern.CHECK)
@Component
class CheckStrategy : PatternStrategy {
    override fun isMatch(
        messageChain: MessageChain,
        botPrefix: String?,
        command: String?,
        vararg alias: String?,
    ): Boolean {
        val singleMessage = messageChain[1]
        return singleMessage is PlainText && messageChain.size == 2
    }
}