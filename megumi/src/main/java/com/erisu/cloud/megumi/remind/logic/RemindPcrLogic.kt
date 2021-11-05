package com.erisu.cloud.megumi.remind.logic

import com.erisu.cloud.megumi.remind.pojo.Calendar
import com.erisu.cloud.megumi.remind.pojo.RemindType
import com.erisu.cloud.megumi.util.FileUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
     * 获取某日活动
     *
     * @param day xxxx/xx/xx
     */
    fun getDayActivities(day: String, types: List<RemindType>): List<Calendar> {
        val calendarFile =
            File("${FileUtil.localStaticPath}${File.separator}basic${File.separator}calendar${File.separator}jp.json")
        val json = calendarFile.readLines().joinToString(separator = "")
        val calendars: List<Calendar> = Json.decodeFromString(json)
        return calendars.filter {
            val s = it.start_time.split(" ")[0].split("/")
            val n = day.split("/")
            s[0].toInt() >= n[0].toInt()
                    && s[1].toInt() >= n[1].toInt()
                    && s[2].toInt() >= n[2].toInt()
                    && types.any { t -> t.name == it.type }
        }
    }
}

fun main() {
//    RemindPcrLogic().getDayActivities("2021/11/05")
}