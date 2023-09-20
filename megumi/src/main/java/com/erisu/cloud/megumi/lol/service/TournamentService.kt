package com.erisu.cloud.megumi.lol.service

import cn.hutool.core.date.DateUnit
import cn.hutool.core.date.DateUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.lol.logic.LplSpringLogic
import com.erisu.cloud.megumi.lol.logic.TournamentLogic
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Model(name = "lol赛事", help =
"""
正在加紧研发中~
""")
@Component
class TournamentService {
    //    @Resource
//    private lateinit var tournamentLogic: TournamentLogic
    @Resource
    private lateinit var lplSpringLogic: LplSpringLogic

    @Command(commandType = CommandType.GROUP, value = "(今日)?赛程", pattern = Pattern.REGEX)
    fun searchTodayLpl(sender: User, messageChain: MessageChain, subject: Contact): Message {
        return lplSpringLogic.getDaySpringData(null)
    }

    @Command(commandType = CommandType.GROUP, value = "明[日天]赛程", pattern = Pattern.REGEX)
    fun searchTomorrowLpl(sender: User, messageChain: MessageChain, subject: Contact): Message {
        return lplSpringLogic.getDaySpringData(DateUtil.formatDate(DateUtil.tomorrow()))
    }

    @Command(commandType = CommandType.GROUP, value = "([0-9]{2}-[0-9]{2})赛程", pattern = Pattern.REGEX)
    fun searchDayLpl(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val day = Regex("([0-9]{2}-[0-9]{2})赛程").find(messageChain.contentToString())!!.groupValues[1]
        return lplSpringLogic.getDaySpringData("2023-$day")
    }

    @Command(commandType = CommandType.GROUP, value = "查赛程", pattern = Pattern.PREFIX)
    fun searchTeamLpl(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val team = messageChain.contentToString().removePrefix("查赛程").trim().uppercase()
        return lplSpringLogic.getTeamSpringData(team)
    }


}
