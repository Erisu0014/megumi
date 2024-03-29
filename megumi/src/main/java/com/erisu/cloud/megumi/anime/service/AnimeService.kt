package com.erisu.cloud.megumi.anime.service

import com.erisu.cloud.megumi.anime.logic.AnimeLogic
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
 *@Description 动画相关
 *@Author alice
 *@Date 2021/6/23 18:23
 **/
@Component
@Model(name = "二次元", help =
"""
[/二 ...]：
""")
class AnimeService {
    @Resource
    lateinit var animeLogic: AnimeLogic

    @Command(value = "/二",
        commandType = CommandType.GROUP,
        pattern = Pattern.PREFIX, alias = ["/2"])
    fun randomMemories(sender: User?, messageChain: MessageChain, subject: Contact?): Message {
        val from = messageChain.contentToString().substring(2).trim()
        return animeLogic.getRandomMemories(from)
    }


}


