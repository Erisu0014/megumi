package com.erisu.cloud.megumi.remind.logic

import net.mamoe.mirai.message.data.*

object RemindTestLogic {
    fun remindMe(messageChain: MessageChain): Message {
        val messages = mutableListOf<Message>()
        messageChain.forEach { if (it.toString() != "!提醒") messages.add(it) }
        val chainMessage: Array<Message> = messages.toTypedArray()
        return messageChainOf(*chainMessage)
    }

}