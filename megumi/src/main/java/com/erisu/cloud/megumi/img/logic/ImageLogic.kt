package com.erisu.cloud.megumi.img.logic

import com.erisu.cloud.megumi.util.ImageUtil
import com.erisu.cloud.megumi.util.MessageUtil.generateImage
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.CompletableFuture
import javax.annotation.Resource

/**
 *@Description 图片处理逻辑
 *@Author alice
 *@Date 2021/6/24 10:22
 **/
@Slf4j
@Component
class ImageLogic {
    @Resource
    private lateinit var imageUtil: ImageUtil

    @Throws(Exception::class)
    suspend fun generateImage(sender: User, subject: Contact, text: String): Message {
        val imgPath = imageUtil.generateImage("emoticon/xcw-1.jpg", text)
        // 此处为外置路径可获取file
        return generateImage(subject as Group, File(imgPath), true)
    }
}
