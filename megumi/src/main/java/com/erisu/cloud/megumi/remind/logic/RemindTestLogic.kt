package com.erisu.cloud.megumi.remind.logic

import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf

object RemindTestLogic {
    fun remindMe(messageChain: MessageChain): MessageChain {
        val messages = mutableListOf<Message>()
        messageChain.forEachIndexed { index, it ->
            if (!it.toString().startsWith("todo") || index != 1) messages.add(it)
            else if (it is PlainText) {
                val temp = it.toString().removePrefix("todo").trim()
                if (temp.isNotBlank()) {
                    messages.add(PlainText(temp))
                }
            }
        }
        val chainMessage: Array<Message> = messages.toTypedArray()
        return messageChainOf(*chainMessage)
    }

}