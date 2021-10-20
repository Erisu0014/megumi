package com.erisu.cloud.megumi.pattern.strategy

import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.pattern.PatternStrategy
import com.erisu.cloud.megumi.pattern.PatternSupport
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChain
import org.springframework.stereotype.Component

/**
 *@Description TODO
 *@Author alice
 *@Date 2021/6/24 9:00
 **/
@PatternSupport(pattern = Pattern.CHECK_AT)
@Component
class CheckAtStrategy : PatternStrategy {
    /**
     *
     *
     * @param messageChain
     * @param botPrefix 在此处无作用
     * @param command
     * @param alias
     * @return
     */
    override fun isMatch(
        messageChain: MessageChain,
        botPrefix: String,
        command: String,
        vararg alias: String?,
    ): Boolean {
        return messageChain[1] is At
    }
}