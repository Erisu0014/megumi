package com.erisu.cloud.megumi.util

import kotlinx.coroutines.*
import kotlinx.coroutines.future.future
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.CompletableFuture

/**
 *@Description 消息处理器
 *@Author alice
 *@Date 2021/6/22 14:37
 **/
object StreamMessageUtil {
    fun message(vararg message: Message): Message {
        return messageChainOf(*message)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun generateImageAsync(group: Group, file: File, delete: Boolean): CompletableFuture<Image> =
        GlobalScope.future { generateImage(group, file, delete) }

    @OptIn(DelicateCoroutinesApi::class)
    fun generateImageAsync(group: Group, inputStream: InputStream): CompletableFuture<Image> =
        GlobalScope.future { generateImage(group, inputStream) }

    suspend fun generateImage(group: Group, file: File, delete: Boolean): Image {
        val externalResource = file.toExternalResource()
        val image: Image = group.uploadImage(externalResource)
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                externalResource.close()
            }
        }
        if (delete) file.delete()
        return image
    }

    suspend fun generateAudio(group: Group, file: File, delete: Boolean): Audio {
        val externalResource = file.toExternalResource()
        val audio: OfflineAudio = group.uploadAudio(externalResource)
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                externalResource.close()
            }
        }
        if (delete) file.delete()
        return audio
    }

    suspend fun generateAudio(group: Group, inputStream: InputStream): Audio {
        val externalResource = inputStream.toExternalResource()
        val audio: OfflineAudio = group.uploadAudio(externalResource)
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                externalResource.close()
                inputStream.close()
            }
        }
        return audio
    }

    suspend fun generateAudio(group: Group, inputStream: InputStream, formatName: String): Audio {
        val externalResource = inputStream.toExternalResource(formatName)
        val audio: OfflineAudio = group.uploadAudio(externalResource)
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                externalResource.close()
                inputStream.close()
            }
        }
        return audio
    }

    @Throws(IOException::class)
    suspend fun generateImage(group: Group, inputStream: InputStream): Image {
        val externalResource = inputStream.toExternalResource()
        val image: Image = group.uploadImage(externalResource)
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                externalResource.close()
                inputStream.close()
            }
        }
        return image
    }


}