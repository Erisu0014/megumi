package com.erisu.cloud.megumi.pixiv.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.pixiv.logic.PixivLogic
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description 涉及pixiv相关的问答
 *@Author alice
 *@Date 2021/10/8 14:43
 **/
@Model(name = "pixiv")
@Component
class PixivService {
    @Resource
    private lateinit var pixivLogic: PixivLogic

    @Command(
        commandType = CommandType.GROUP,
        pattern = Pattern.REGEX,
        value = "https?://www.pixiv.net/artworks/(.+)",
        uuid = "92d7d54b7803459e8b8a2a6b1dbffe2d"
    )
    suspend fun pixivToCat(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        return pixivLogic.getPixivCatImage(messageChain, subject)
    }
}