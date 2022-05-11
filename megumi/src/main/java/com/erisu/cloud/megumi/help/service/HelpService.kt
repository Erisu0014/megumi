package com.erisu.cloud.megumi.help.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.help.logic.HelpLogic
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Description get help info
 * @Author alice
 * @Date 2021/10/11 10:41
 */
@Model(name = "帮助",help =
"""
[ll]：查询bot挂载服务列表
[帮助 ...]：查询该服务的帮助信息
""")
@Component
class HelpService {
    @Resource
    private lateinit var helpLogic: HelpLogic

    @Command(commandType = CommandType.GROUP, value = "ll", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    fun ll(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        return helpLogic.getModelList()
    }


    @Command(commandType = CommandType.GROUP, value = "帮助", pattern = Pattern.PREFIX)
    @Throws(Exception::class)
    fun help(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val modelName = messageChain.content.removePrefix("帮助").trim()
        return helpLogic.getHelpInfo(modelName)
    }


}