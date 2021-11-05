package com.erisu.cloud.megumi.remind.service

import cn.hutool.core.util.StrUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.remind.logic.RemindMeLogic
import com.erisu.cloud.megumi.remind.logic.RemindPcrLogic
import com.erisu.cloud.megumi.remind.logic.RemindTestLogic.remindMe
import com.erisu.cloud.megumi.remind.pojo.DevRemind
import com.erisu.cloud.megumi.remind.pojo.RemindType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChain.Companion.serializeToJsonString
import net.mamoe.mirai.message.data.MessageChain.Companion.serializeToString
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Description 用以存储一些开发中任务及灵感(为什么你不用todo呢
 * @Author alice
 * @Date 2021/6/11 16:30
 */
@ExperimentalSerializationApi
@Component
@Model(name = "remind")
class RemindService {
    @Resource
    private lateinit var remindMeLogic: RemindMeLogic

    @Resource
    private lateinit var remindPcrLogic: RemindPcrLogic

    @Command(value = "todo", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    fun addRemindMe(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val group = subject as Group
        val message = remindMe(messageChain)
        val messageStr: String = messageChain.serializeToJsonString()
        remindMeLogic.addRemindMe(DevRemind(null, group.id.toString(), sender.id.toString(), messageStr))
        return PlainText("已添加提醒事项")
    }

    @Command(value = "/todo", commandType = CommandType.GROUP, pattern = Pattern.EQUALS)
    fun showRemindMe(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val group = subject as Group
        return remindMeLogic.getRemindMe(group.id.toString(), sender.id.toString())
    }

    @Command(value = "~todo", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    fun removeRemindMe(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val group = subject as Group
        val (content) = messageChain[1] as PlainText
        val id = StrUtil.removePrefix(content, "~todo").trim { it <= ' ' }
        return if (StrUtil.isBlank(id)) {
            null
        } else remindMeLogic.removeRemindMe(id, group.id.toString(), sender.id.toString())
    }

    @Command(value = "testb([0-9]{4}/[0-9]{2}/[0-9]{2})", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    fun calendarBegin(sender: User?, messageChain: MessageChain, subject: Contact?): Message? {
        val day = Regex("testb([0-9]{4}/[0-9]{2}/[0-9]{2})").find(messageChain.contentToString())!!.groupValues[1]
        val types = RemindType.values().filter { it.remindBegin == 1 } ?: return null
        return if (types.isEmpty()) {
            null
        } else {
            val activities = remindPcrLogic.getDayActivities(day, types)
            PlainText(activities.joinToString(separator = "\n"))
        }
    }
}