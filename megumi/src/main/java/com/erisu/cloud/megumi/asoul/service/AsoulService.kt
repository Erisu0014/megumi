package com.erisu.cloud.megumi.asoul.service

import com.erisu.cloud.megumi.asoul.logic.AsoulLogic
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description 查重
 *@Author alice
 *@Date 2021/8/27 14:41
 **/
@Component
@Model(name = "查重",help =
"""
[查重 ...]：枝网查重专用，似乎不是枝网的也可以查
""")
class AsoulService {
    @Resource
    private lateinit var asoulLogic: AsoulLogic

    @Command(value = "查重", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    fun doubleCheck(sender: User?, messageChain: MessageChain, subject: Contact?): Message? {
        val content = messageChain.contentToString()
        return asoulLogic.doubleCheck(content.trim())
    }

    @Command(value = "",
        commandType = CommandType.GROUP,
        pattern = Pattern.CHECK,
        uuid = "0bc353b425a1409bb4c9a473d8bab737")
    fun doubleCheckDefault(sender: User?, messageChain: MessageChain, subject: Contact?): Message? {
        val content = messageChain.contentToString()
        if (content.length > 400) {
            return asoulLogic.doubleCheck(content.trim())
        }
        return null
    }
}