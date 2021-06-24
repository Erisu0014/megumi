package com.erisu.cloud.megumi.img.service

import cn.hutool.core.util.StrUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.img.logic.ImageLogic
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description 图片相关
 *@Author alice
 *@Date 2021/6/24 10:35
 **/
@Model(name = "img")
@Component
class ImgService {
    @Resource
    private lateinit var imageLogic: ImageLogic

    @Command(commandType = CommandType.GROUP,
        value = ".jpg",
        pattern = Pattern.SUFFIX,
        uuid = "ff33a026c184430fb88fb1e49ee4bf25")
    @Throws(Exception::class)
    fun generateImg(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val (content) = messageChain[1] as PlainText
        val text = content.removeSuffix(".jpg")
        return GlobalScope.future { imageLogic.generateImage(sender, subject, text) }.get()
    }
}