package com.erisu.cloud.megumi.event

import com.erisu.cloud.megumi.emoji.logic.PcrEmojiLogic
import com.erisu.cloud.megumi.setu.logic.SetuLogic
import com.erisu.cloud.megumi.song.logic.MusicLogic
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MemberCardChangeEvent
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.NudgeEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import org.springframework.core.io.ClassPathResource
import java.io.File
import javax.annotation.Resource
import kotlin.random.Random

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

    @Resource
    private lateinit var setuLogic: SetuLogic

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
        val contact = event.from as NormalMember
        val group = contact.group
        val nudge = contact.nudge()
        if (event.from !is NormalMember) {
            return ListeningStatus.LISTENING
        }
        //  迫害诗酱小助手
        if (event.target.id == 3099396879L) {
            val randomFile =
                FileUtil.getRandomFile("${FileUtil.localStaticPath}${File.separator}memento", "png")
            val image = StreamMessageUtil.generateImage(group, File(randomFile), false)
            group.sendMessage(image)
            return ListeningStatus.LISTENING
        }
        if (event.target.id != bot.id) {
            return ListeningStatus.LISTENING
        }


        val random = Math.random()
        when {
            random < 0.01 -> {
                val image =
                    StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/kyaru.gif").inputStream)
                val msg = "──●━━━━ 1:05/1:30\n" + "正在播放：New Year Burst\n" + "⇆ ㅤ◁ ㅤㅤ❚❚ ㅤㅤ▷ ㅤ↻"
                group.sendMessage(messageChainOf(image, PlainText(msg)))
            }
            // 随机表情
            random < 0.3 -> {
                group.sendMessage(emojiLogic.getRandomImage(group)!!)
            }
            random < 0.5 -> {
                val path = "${FileUtil.localStaticPath}${File.separator}hutao"
                val randomFile = FileUtil.getRandomFile(path, "mp3")
                group.sendMessage(StreamMessageUtil.generateAudio(group,
                    File(randomFile).inputStream()))
            }
            // 来点setu
            random < 0.6 -> {
                setuLogic.getRollSetu("", 1, 0, group)
            }
            // 来点歌
            random < 0.8 -> {
                musicLogic.getRandomSongs()?.let { group.sendMessage(it) }
            }
            // 戳出事了你负责吗
            else -> {
                nudge.sendTo(group)
                group.sendMessage("戳出事了你负责吗~")
            }
        }
        return ListeningStatus.LISTENING // 表示继续监听事件
    }
}