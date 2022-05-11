package com.erisu.cloud.megumi.tekoki.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.tekoki.logic.TekokiLogic
import com.erisu.cloud.megumi.util.StreamMessageUtil
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description tekoki
 *@Author alice
 *@Date 2021/9/16 9:19
 **/
@Slf4j
@Component
@Model(name = "tekoki",help =
"""不想写""")
class TekokiService {
    @Resource
    private lateinit var tekokiLogic: TekokiLogic

    @Command(commandType = CommandType.GROUP, value = "今天冲什么", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun tekoki(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        return tekokiLogic.tekoki(sender, subject as Group)
    }

}