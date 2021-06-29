package com.erisu.cloud.megumi.event

import com.erisu.cloud.megumi.emoji.logic.PcrEmojiLogic
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MemberCardChangeEvent
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.NudgeEvent
import javax.annotation.Resource

/**
 *@Description 群组事件[主动监听发起]
 *@Author alice
 *@Date 2021/6/25 16:32
 **/
@Event
class BaseGroupEvent : SimpleListenerHost() {
    @Resource
    private lateinit var emojiLogic: PcrEmojiLogic

    /**
     * 入群事件
     *
     * @param event
     * @return
     */
    @EventHandler(priority = EventPriority.NORMAL)
    suspend fun onMemberJoinEvent(event: MemberJoinEvent): ListeningStatus {
        val name = event.member.nameCard
        event.group.sendMessage(String.format("欢迎%s进群~", name))
        return ListeningStatus.LISTENING // 表示继续监听事件
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onMemberCardChangeEvent(event: MemberCardChangeEvent): ListeningStatus {
//        String name = event.getMember().getNameCard();
//        event.getGroup().sendMessage(String.format("欢迎%s进群~", name));
        return ListeningStatus.LISTENING // 表示继续监听事件
    }


    /**
     * 戳出事了你负责吗
     *
     * @param event
     * @return
     */
    @EventHandler(priority = EventPriority.NORMAL)
    suspend fun onNudgedEvent(event: NudgeEvent): ListeningStatus {
        val bot = event.bot
        if (event.from !is NormalMember) {
            return ListeningStatus.LISTENING // 表示继续监听事件
        }
        val contact = event.from as NormalMember
        val group = contact.group
        val nudge = contact.nudge()
        nudge.sendTo(group)
        if (Math.random() > 0.5) {
            bot.getGroup(contact.group.id)!!.sendMessage(emojiLogic.getRandomImage(group)!!)
        }
        return ListeningStatus.LISTENING // 表示继续监听事件
    }
}