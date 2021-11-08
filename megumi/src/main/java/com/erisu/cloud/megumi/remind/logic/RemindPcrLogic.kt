package com.erisu.cloud.megumi.remind.logic

import cn.hutool.core.date.DateUtil
import com.erisu.cloud.megumi.remind.pojo.Calendar
import com.erisu.cloud.megumi.remind.pojo.RemindType
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File

/**
 *@Description remind pcr
 *@Author alice
 *@Date 2021/11/5 14:59
 **/
@Component
class RemindPcrLogic {

    /**
     * 获取明日活动并提醒
     *
     * @param day xxxx/xx/xx
     */
    suspend fun getTomorrowActivities(group: Group, day: String, types: List<RemindType>): Message? {
        val calendarFile =
            File("${FileUtil.localStaticPath}${File.separator}basic${File.separator}calendar${File.separator}jp.json")
        val json = calendarFile.readLines().joinToString(separator = "")
        val calendars: List<Calendar> = Json.decodeFromString(json)
        val filter = calendars.filter {
            val s = it.start_time.split(" ")[0].replace("/", "-")//转化为yyyy-MM-dd
            val itTime = DateUtil.parseDate(s)
            val now = DateUtil.parseDate(day)
            itTime == DateUtil.offsetDay(now, 1) && types.any { t -> t.name == it.type }
        }
        if (filter.isEmpty()) return null
        val message: String = "明天将要开始的活动有：\n" + filter.joinToString(separator = "\n") { it.name } + "\n骑士君做好准备哦~"
        val image = StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/happy.gif").inputStream)
        return messageChainOf(PlainText(message), image)
    }

    /**
     * 获取今日活动并提醒
     *
     * @param day xxxx/xx/xx
     */
    suspend fun getTodayActivities(group: Group, day: String, types: List<RemindType>): Message? {
        val calendarFile =
            File("${FileUtil.localStaticPath}${File.separator}basic${File.separator}calendar${File.separator}jp.json")
        val json = calendarFile.readLines().joinToString(separator = "")
        val calendars: List<Calendar> = Json.decodeFromString(json)
        val filter = calendars.filter {
            val s = it.end_time.split(" ")[0].replace("/", "-")//转化为yyyy-MM-dd
            val itTime = DateUtil.parseDate(s)
            val now = DateUtil.parseDate(day)
            itTime == now && types.any { t -> t.name == it.type }
        }
        if (filter.isEmpty()) return null
        val message: String =
            "今天将要结束的活动有：\n" + filter.joinToString(separator = "\n") {it.name+"\t${it.end_time.split(" ")[1]}\uD83C\uDF89" } + "\n骑士君还没有那个吗？"
        val image = StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/雪哥问号.jpg").inputStream)
        return messageChainOf(PlainText(message), image)
    }

}

fun main() {
//    RemindPcrLogic().getDayActivities("2021/11/05")
}