package com.erisu.cloud.megumi.battle.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/1/30 11:28
 **/
@Component
@Model(name = "简易公会战", help = "在做了在做了")
class BattleService {
    @Command(value = "预约([1-5])+", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    @Throws(
        Exception::class
    )
    fun orderBoss(sender: User?, messageChain: MessageChain?, subject: Contact?): Message? {
        val group = subject as Group?
        return null
    }
}