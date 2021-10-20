package com.erisu.cloud.megumi.pcr.basic.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.pcr.basic.logic.GachaLogic
import com.erisu.cloud.megumi.pcr.basic.logic.NameLogic
import com.erisu.cloud.megumi.pcr.basic.logic.PcrInitData
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description pcr 基本信息相关
 *@Author alice
 *@Date 2021/6/24 10:27
 **/
@Model(name = "name")
@Component
class PcrBasicService {
    @Resource
    private lateinit var nameLogic: NameLogic

    @Resource
    private lateinit var gachaLogic: GachaLogic

    @Resource
    private lateinit var redisUtil: RedisUtil

    @Resource
    private lateinit var pcrInitData: PcrInitData

    @Command(
        commandType = CommandType.GROUP,
        value = "谁是",
        pattern = Pattern.PREFIX,
        uuid = "d92fcb894ba34f81a108194e219999bb"
    )
    @Throws(Exception::class)
    suspend fun searchName(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val (content) = messageChain[1] as PlainText
        val name = content.removePrefix("谁是").trim()
        var princessId = pcrInitData.nameMap[name]
        if (princessId == null) {
            princessId = pcrInitData.nameMap["${sender.id}::$name"]
        }
        return if (princessId == null) {
            PlainText("兰德索尔似乎没有叫${name}的人...")
        } else {
            nameLogic.getAvatar(sender, subject as Group, princessId)
        }
    }

    /**
     * 向现有库添加昵称
     *
     * @param sender
     * @param messageChain
     * @param subject
     * @return
     * @throws Exception
     */
    @Command(
        commandType = CommandType.GROUP,
        value = "添加昵称 (.+) (.+)",
        pattern = Pattern.REGEX,
        uuid = "35eb96b99d944a6b9bcc3d066407f6f4"
    )
    @Throws(Exception::class)
    fun addNickName(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        // TODO: 2021/10/20 group分组
        val content = messageChain.contentToString()
        val finder = Regex("添加昵称 (.+) (.+)").find(content) ?: return null
        val oldName = finder.groupValues[1]
        val newName = finder.groupValues[2]
        val nameMap = pcrInitData.nameMap
        val id = nameMap[oldName]
        return if (id != null) {
            if (!nameMap.containsKey("${sender.id}::$newName")) {
                nameMap["${sender.id}::$newName"] = id
                redisUtil.hPut(RedisKey.PRINCESS_NAME.key + "::" + sender.id, newName, id)
                PlainText("添加成功~")
            } else PlainText("已经有人叫这个名字了哦")
        } else null
    }


    @Command(commandType = CommandType.GROUP, value = "体检", pattern = Pattern.PREFIX)
    @Throws(Exception::class)
    suspend fun checkProfile(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val (content) = messageChain[1] as PlainText
        val name = content.removePrefix("体检").trim()
        val character = pcrInitData.nameMap[name]
        return if (character == null) {
            PlainText("无体检数据")
        } else {
            val pcrPrincess = pcrInitData.princessMap[character]
            val image = nameLogic.getAvatarImage(subject as Group, character) ?: return PlainText("体检发生异常，请联系alice")
            return if (pcrPrincess != null) messageChainOf(At(sender.id), image, PlainText(pcrPrincess.toString()))
            else PlainText("无体检数据")
        }
    }

//    @Command(commandType = CommandType.GROUP, value = "更新卡池", pattern = Pattern.PREFIX)
//    fun updateGacha(sender: User, messageChain: MessageChain, subject: Contact?): Message {
//        return gachaLogic.updateUser(sender, messageChain, subject as Group)
//    }
//
//
//    @Command(commandType = CommandType.GROUP, value = "alice来一井", pattern = Pattern.PREFIX, alias = ["来一井"])
//    fun getGacha(sender: User, messageChain: MessageChain, subject: Contact?): Message {
//        return gachaLogic.getGacha(sender, messageChain, subject as Group)
//    }


    /**
     * 谁的xx是xx
     */
    @Command(commandType = CommandType.GROUP, value = "谁的(.+)是(.+)", pattern = Pattern.REGEX)
    fun findCharacter(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        return nameLogic.findCharacter(sender, messageChain.contentToString(), subject as Group, "谁的(.+)是(.+)")
    }


}