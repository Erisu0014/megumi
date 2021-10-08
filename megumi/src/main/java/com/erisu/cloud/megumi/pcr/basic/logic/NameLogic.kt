package com.erisu.cloud.megumi.pcr.basic.logic

import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrPrincess
import com.erisu.cloud.megumi.pcr.basic.util.CharacterField
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
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

    @Resource
    private lateinit var gachaLogic: GachaLogic

    @Throws(Exception::class)
    suspend fun getAvatar(sender: User, group: Group, princessId: String): Message {
        val avatarImage = getAvatarImage(group, princessId)
        return if (avatarImage == null) {
            messageChainOf(
                At(sender.id),
                PlainText(pcrInitData.idMap[princessId]!![0])
            )
        } else {
            messageChainOf(
                At(sender.id),
                avatarImage,
                PlainText(pcrInitData.idMap[princessId]!![0])
            )
        }

    }


    @Throws(Exception::class)
    suspend fun getAvatarImage(group: Group, princessId: String): Message? {
        val (_, _, _, avatarUrl) = avatarMapper.searchMaxStarAvatar(princessId)
        val imageResponse = FileUtil.downloadHttpUrl(avatarUrl, "image", null, null) ?: return null
        if (imageResponse.code != 200) return null
        return StreamMessageUtil.generateImage(group, imageResponse.path!!.toFile(), true)
    }

    fun findCharacter(sender: User, rawMessage: String, group: Group, regexStr: String): Message {
        val regex = Regex(regexStr)
        val type = regex.find(rawMessage)!!.groupValues[1]
        val value = regex.find(rawMessage)!!.groupValues[2]
        val characterField = CharacterField.values().first { it.key == type }
        val properties = PcrPrincess::class.java.declaredFields
        val resultList: MutableList<Int> = mutableListOf()
        pcrInitData.princessMap.forEach { (t, u) ->
            val get = properties.first { it.name == characterField.field }.get(u)
            if (get == value) resultList.add(t.toInt())
        }
        if (resultList.isEmpty()) {
            return PlainText("未找到符合的人选")
        }
        val spliceCacheImage = gachaLogic.spliceCacheImage(resultList, group)
        return messageChainOf(At(sender.id), PlainText("您可能在找:"), spliceCacheImage)
    }

}
