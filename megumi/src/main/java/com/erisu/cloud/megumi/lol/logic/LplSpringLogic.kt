package com.erisu.cloud.megumi.lol.logic

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
        var searchDay = day ?: DateUtil.today()
        return if (daySpringData.containsKey(searchDay)) {
            val dayList = daySpringData[searchDay]
            result += "今天为第${dayList!![0].week}周比赛日\n"
            dayList.forEach { m -> result += "${m.time.split(" ")[1]}\t${m.b} vs ${m.r}\n" }
            PlainText(result)
        } else {
            PlainText("今天没有比赛哦~")
        }
    }
}