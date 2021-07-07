package com.erisu.cloud.megumi.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.Voice
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File
import java.io.InputStream
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

    fun generateImageAsync(group: Group, inputStream: InputStream): CompletableFuture<Image> =
        GlobalScope.future { generateImage(group, inputStream) }

    suspend fun generateImage(group: Group, file: File, delete: Boolean): Image {
        val externalResource = file.toExternalResource()
        val image: Image = group.uploadImage(externalResource)
        externalResource.close()
        if (delete) file.delete()
        return image
    }

    suspend fun generateAudio(group: Group, file: File, delete: Boolean): Voice {
        val externalResource = file.toExternalResource()
        val voice: Voice = group.uploadVoice(externalResource)
        externalResource.close()
        if (delete) file.delete()
        return voice
    }

    suspend fun generateAudio(group: Group, inputStream: InputStream): Voice {
        val externalResource = inputStream.toExternalResource()
        val voice: Voice = group.uploadVoice(externalResource)
        externalResource.close()
        return voice
    }

    suspend fun generateImage(group: Group, inputStream: InputStream): Image {
        val externalResource = inputStream.toExternalResource()
        val image: Image = group.uploadImage(externalResource)
        externalResource.close()
        inputStream.close()
        return image
    }


}