package com.erisu.cloud.megumi.lol.logic

import cn.hutool.core.date.DatePattern
import cn.hutool.core.date.DateUtil
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.Resource

@Component
class LplSpringLogic {
    @Resource
    private lateinit var lplSpringInitData: LplSpringInitData

    private fun getSpringData() {
        if (!lplSpringInitData.isInited) {
            lplSpringInitData.initData()
        }
    }

    fun getDaySpringData(day: String?): Message {
        getSpringData()
        val daySpringData = lplSpringInitData.daySpringData
        var result = ""
        val searchDay = day ?: DateUtil.today()
        return if (daySpringData.containsKey(searchDay)) {
            val dayList = daySpringData[searchDay]
            result += "今天为第${dayList!![0].week}周比赛日\n"
            dayList.forEach { result += "${it.time.split(" ")[1]}\t${it.b} vs ${it.r}\n" }
            PlainText(result)
        } else {
            PlainText("今天没有比赛哦~")
        }
    }

    fun getTeamSpringData(team: String): Message {
        getSpringData()
        var result = ""
        val teamSpringData = lplSpringInitData.teamSpringData
        return if (teamSpringData.containsKey(team)) {
            result += "${team}剩余的比赛还有：\n"
            val lastLpl = teamSpringData[team]!!.filter {
                DateUtil.compare(DateUtil.parse(it.time, DatePattern.NORM_DATETIME_MINUTE_PATTERN),
                    DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN)) > 0
            }
            if (lastLpl.isEmpty()) {
                PlainText("${team}的夏季赛可能结束了~")
            } else {
                lastLpl.forEach { result += "${it.time.substring(5)}\t${it.b} vs ${it.r}\n" }
                PlainText(result)
            }
        } else {
            PlainText("找不到该战队的比赛>.<它是LPL队伍吗")
        }
    }


}