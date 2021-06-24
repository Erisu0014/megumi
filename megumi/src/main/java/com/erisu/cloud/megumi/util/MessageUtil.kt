package com.erisu.cloud.megumi.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 *@Description 消息处理器
 *@Author alice
 *@Date 2021/6/22 14:37
 **/
object MessageUtil {
    fun message(vararg message: Message): Message {
        return messageChainOf(*message)
    }


    fun generateImageAsync(group: Group, file: File, delete: Boolean): CompletableFuture<Image> =
        GlobalScope.future { generateImage(group, file, delete) }

    suspend fun generateImage(group: Group, file: File, delete: Boolean): Image {
        val externalResource = file.toExternalResource()
        val image: Image = group.uploadImage(externalResource)
        externalResource.close()
        if (delete) file.delete()
        return image
    }

}