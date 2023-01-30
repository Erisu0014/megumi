package com.erisu.cloud.megumi.remind.service

import cn.hutool.core.date.DateUtil
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChain.Companion.serializeToJsonString
import net.mamoe.mirai.message.data.MessageChain.Companion.serializeToString
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Description 用以存储一些开发中任务及灵感(为什么你不用todo呢
 * @Author alice
 * @Date 2021/6/11 16:30
 */
@ExperimentalSerializationApi
@Component
@Model(name = "remind",help =
"""
[todo ...]：add something todo
[/todo]：show some todo
[~todo id]:remove todo by id
""")
class RemindService {
    @Value("\${qq.username}")
    private var username: Long = 0

    @Resource
    private lateinit var remindMeLogic: RemindMeLogic

    @Resource
    private lateinit var remindPcrLogic: RemindPcrLogic

    @Command(value = "todo", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    fun addRemindMe(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val group = subject as Group
        val message = remindMe(messageChain)
        val messageStr: String = message.serializeToJsonString()
        remindMeLogic.addRemindMe(DevRemind(null, group.id.toString(), sender.id.toString(), messageStr))
        return PlainText("已添加提醒事项")
    }

    @Command(value = "/todo", commandType = CommandType.GROUP, pattern = Pattern.EQUALS)
    fun showRemindMe(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val group = subject as Group
        return remindMeLogic.getRemindMe(group.id.toString(), sender.id.toString())
    }

    @Command(value = "~todo", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    suspend fun removeRemindMe(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val group = subject as Group
        val (content) = messageChain[1] as PlainText
        val id = StrUtil.removePrefix(content, "~todo").trim { it <= ' ' }
        return if (StrUtil.isBlank(id)) {
            null
        } else remindMeLogic.removeRemindMe(id, group, sender.id.toString())
    }

//    /**
//     * 活动开始前提醒测试代码
//     *
//     * @param sender
//     * @param messageChain
//     * @param subject
//     * @return
//     */
//    @Scheduled(cron = "0 00 18 * * ?")
//    fun calendarBegin() {
//        GlobalScope.future {
//            val today = DateUtil.today()
//            val types = RemindType.values().filter { it.remindBegin == 1 }
//            if (types.isEmpty()) return@future
//            val bot = Bot.getInstance(username)
//            val group = bot.getGroup(823621066L) ?: return@future
//            val message = remindPcrLogic.getTomorrowActivities(group, today, types) ?: return@future
//            group.sendMessage(message)
//        }
//    }

//    /**
//     * 活动结束前提醒测试代码
//     *
//    //     * @param sender
//    //     * @param messageChain
//    //     * @param subject
//     * @return
//     */
//    @Scheduled(cron = "0 30 10 * * ?")
//    fun calendarEnd() {
//        GlobalScope.future {
//            val today = DateUtil.today()
//            val types = RemindType.values().filter { it.remindLast == 1 }
//            if (types.isEmpty()) return@future
//            val bot = Bot.getInstance(username)
//            val group = bot.getGroup(823621066L) ?: return@future
//            val message = remindPcrLogic.getTodayActivities(group, today, types) ?: return@future
//            group.sendMessage(message)
//        }
//    }
}