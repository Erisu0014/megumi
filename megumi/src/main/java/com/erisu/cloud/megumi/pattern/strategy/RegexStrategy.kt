package com.erisu.cloud.megumi.pattern.strategy

import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.pattern.PatternStrategy
import com.erisu.cloud.megumi.pattern.PatternSupport
import net.mamoe.mirai.message.data.MessageChain
import org.springframework.stereotype.Component

/**
 *@Description TODO
 *@Author alice
 *@Date 2021/6/24 9:00
 **/
@PatternSupport(pattern = Pattern.PREFIX)
@Component
class PrefixStrategy : PatternStrategy {
    /**
     * 正则匹配不需要alias(暂时)
     *
     * @param messageChain
     * @param command
     * @param alias
     * @return
     */
    override fun isMatch(messageChain: MessageChain, command: String, vararg alias: String?): Boolean {
        val regex = Regex(command)
        return regex.containsMatchIn(messageChain[1].toString())
    }
}