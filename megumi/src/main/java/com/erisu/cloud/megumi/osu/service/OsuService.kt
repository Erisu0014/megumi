package com.erisu.cloud.megumi.osu.service

import cn.hutool.core.lang.UUID
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.osu.logic.OsuLogic
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/2/14 10:51
 **/
@Component
@Model(
    name = "osu", uuid = "", help =
    """
    在写了在写了
"""
)
class OsuService {
    @Resource
    private lateinit var osuLogic: OsuLogic

    @Command(
        commandType = CommandType.GROUP,
        value = "cover([0-9]+)",
        pattern = Pattern.REGEX
    )
    @Throws(Exception::class)
    suspend fun getCover(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val matchResult = Regex("cover([0-9]+)").find(messageChain.contentToString())
        if (matchResult != null) {
            val bid = matchResult.groupValues[1]
            val mapInfo = osuLogic.getMapInfo(bid)
            if (mapInfo != null) {
                val pathRes = FileUtil.downloadHttpUrl(
                    mapInfo.beatmapset.covers.list2x,
                    "cache",
                    null,
                    UUID.fastUUID().toString(true)
                )
                if (pathRes != null && pathRes.code == 200) {
                    return StreamMessageUtil.generateImage(subject as Group, pathRes.path!!.toFile(), false)
                }
            }
        }
        return null
    }

}