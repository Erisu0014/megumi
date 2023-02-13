package com.erisu.cloud.megumi.battle.service

import com.erisu.cloud.megumi.battle.logic.BattleLogic
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/1/30 11:28
 **/
@Component
@Model(name = "简易公会战", help = "不做了，傻逼pcr死了算了，kmr就是个傻逼")
class BattleService {
    @Resource
    private lateinit var battleLogic: BattleLogic

    @Command(value = "预约([1-5一二三四五])+", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    @Throws(Exception::class)
    fun orderBoss(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val message = messageChain.contentToString()
        val result = Regex("预约([1-5一二三四五])+").find(message)
        if (result != null) {
            return PlainText(battleLogic.orderBoss(subject.id.toString(), sender, result.groupValues[1]))
        }
        return null
    }

    @Command(value = "进([1-5一二三四五])+", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    @Throws(Exception::class)
    fun inBoss(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val message = messageChain.contentToString()
        val result = Regex("进([1-5一二三四五])+").find(message)
        if (result != null) {
            return battleLogic.inBoss(subject.id.toString(), sender, result.groupValues[1])
        }
        return null
    }

    @Command(value = "查([1-5一二三四五])+", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    @Throws(Exception::class)
    fun checkBoss(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val message = messageChain.contentToString()
        val result = Regex("查([1-5一二三四五])+").find(message)
        if (result != null) {
            return battleLogic.checkOrderBoss(subject.id.toString(), sender, result.groupValues[1])
        }
        return null
    }

    @Command(value = "check([0-9]{9})+", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
    fun checkId(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val message = messageChain.contentToString()
        val result = Regex("check([0-9]{9})+").find(message)
        if (result != null) {
            return PlainText(battleLogic.checkUid(result.groupValues[1]))
        }
        return PlainText("当前只支持日服风纪区")
    }

}