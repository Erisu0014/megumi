package com.erisu.cloud.megumi.event

import com.erisu.cloud.megumi.emoji.logic.PcrEmojiLogic
import com.erisu.cloud.megumi.song.logic.MusicLogic
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

    @Resource
    private lateinit var musicLogic: MusicLogic

    /**
     * 入群事件
     *
     * @param event
     * @return
     */
    @EventHandler(priority = EventPriority.NORMAL)
    suspend fun onMemberJoinEvent(event: MemberJoinEvent): ListeningStatus {
//        val name = event.member.nameCard
        event.group.sendMessage("你好呀~准备下面试材料哦")
        return ListeningStatus.LISTENING
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
        val random = Math.random()
        if (random < 0.3) {
            bot.getGroup(group.id)!!.sendMessage(emojiLogic.getRandomImage(group)!!)
        } else if (random < 0.8) {
            musicLogic.getRandomSongs()?.let { bot.getGroup(group.id)!!.sendMessage(it) }
        }
        return ListeningStatus.LISTENING // 表示继续监听事件
    }
}