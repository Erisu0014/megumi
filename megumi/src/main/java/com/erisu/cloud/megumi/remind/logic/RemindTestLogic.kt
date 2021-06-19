package com.erisu.cloud.megumi.remind.logic

import cn.hutool.core.util.StrUtil
import net.mamoe.mirai.message.data.*

object RemindTestLogic {
    fun remindMe(messageChain: MessageChain): MessageChain {
        val messages = mutableListOf<Message>()
        messageChain.forEachIndexed { index, it ->
            if (!StrUtil.startWith(it.toString(), "todo")
                || index != 1
            ) messages.add(it) else if (it is PlainText) {
                val temp = StrUtil.removePrefix(it.toString(), "todo").trim()
                if (StrUtil.isNotBlank(temp)) {
                    messages.add(PlainText(temp))
                }
            }
        }
        val chainMessage: Array<Message> = messages.toTypedArray()
        return messageChainOf(*chainMessage)
    }

}