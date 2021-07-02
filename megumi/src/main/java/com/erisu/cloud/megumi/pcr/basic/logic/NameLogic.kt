package com.erisu.cloud.megumi.pcr.basic.logic

import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.MessageUtil
import com.erisu.cloud.megumi.util.MessageUtil.message
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import javax.annotation.Resource

/**
 *@Description NAME相关
 *@Author alice
 *@Date 2021/6/24 10:18
 **/
@Component
class NameLogic {

    @Resource
    private lateinit var avatarMapper: PcrAvatarMapper

    @Resource
    private lateinit var pcrInitData: PcrInitData

    @Throws(Exception::class)
    suspend fun getAvatar(sender: User, group: Group, princessId: String): Message {
        return message(At(sender.id), getAvatarImage(group, princessId), PlainText(pcrInitData.idMap[princessId]!![0]))
    }


    @Throws(Exception::class)
    suspend fun getAvatarImage(group: Group, princessId: String): Message {
        val (_, _, _, avatarUrl) = avatarMapper.searchMaxStarAvatar(princessId)
        val imagePath = FileUtil.downloadHttpUrl(avatarUrl, "image", null)
        return if (imagePath != null) {
            return MessageUtil.generateImage(group, imagePath.toFile(), true)
        } else PlainText("")
    }

}
