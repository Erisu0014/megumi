package com.erisu.cloud.megumi.event

import cn.hutool.core.lang.UUID
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.emoji.logic.PcrEmojiLogic
import com.erisu.cloud.megumi.setu.logic.SetuLogic
import com.erisu.cloud.megumi.song.logic.MusicLogic
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.Memory
import com.erisu.cloud.megumi.util.StreamMessageUtil
import com.erisu.cloud.megumi.util.VoiceUtil
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MemberCardChangeEvent
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.MemberLeaveEvent
import net.mamoe.mirai.event.events.NudgeEvent
import net.mamoe.mirai.message.data.At
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
    private lateinit var voiceUtil: VoiceUtil

    private var lastUsers: MutableMap<String, String> = mutableMapOf()

    /**
     * 入群事件
     *
     * @param event
     * @return
     */
    @EventHandler(priority = EventPriority.NORMAL)
    suspend fun onMemberJoinEvent(event: MemberJoinEvent): ListeningStatus {
//        val name = event.member.nameCard
        event.group.sendMessage("你好呀~")
        val image =
            StreamMessageUtil.generateImage(event.group, ClassPathResource("emoticon/小天使请安.jpg").inputStream)
        if (event.group.id == 823621066L) {
            event.group.sendMessage(messageChainOf(PlainText("日服公会名：lsp同好会\n会长：PaperPig"), image))
        }
        return ListeningStatus.LISTENING
    }

    @EventHandler(priority = EventPriority.NORMAL)
    suspend fun onMemberCardChangeEvent(event: MemberCardChangeEvent): ListeningStatus {
        val name = event.member.nameCard
        event.group.sendMessage(PlainText(name))
        return ListeningStatus.LISTENING // 表示继续监听事件
    }

    @EventHandler(priority = EventPriority.NORMAL)
    suspend fun onMemberLeaveEvent(event: MemberLeaveEvent): ListeningStatus {
        event.group.sendMessage(messageChainOf(PlainText("${event.member.id}退群了\uD83D\uDE36")))
//        val name = event.member.nameCard
//        val avatarUrl = event.member.avatarUrl
//        val fileResponse = FileUtil.downloadHttpUrl(avatarUrl, "cache", null, UUID.fastUUID().toString(true))
//            ?: return ListeningStatus.LISTENING
//        val emoji =
//            StreamMessageUtil.generateImage(event.group, ClassPathResource("emoticon/爱丽丝疑问.jpg").inputStream)
//        event.group.sendMessage(messageChainOf(PlainText("${event.member.id}退群了"), emoji))
//        if (fileResponse.code == 200) {
//            val imageFile = fileResponse.path!!.toFile()
//            val avatar = StreamMessageUtil.generateImage(event.group, imageFile, false)
//            event.group.sendMessage(avatar)
//        }
        return ListeningStatus.LISTENING
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
            return ListeningStatus.LISTENING
        }
        val contact = event.from as NormalMember
        val nudgeId = contact.id
        val group = contact.group
        val nudge = contact.nudge()

        // 判断用户是否发癫
        val dianKey = "${group.id}"
        if (lastUsers[dianKey] == null) lastUsers[dianKey] = "${nudgeId}_1"
        else {
            val split = lastUsers[dianKey]!!.split("_")
            if (nudgeId.toString() == split[0] && split[1].toInt() >= 3) {
                val image =
                    StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/fadian/ui.png").inputStream)
                lastUsers.remove(dianKey)
                group.sendMessage(messageChainOf(At(nudgeId), image))
                return ListeningStatus.LISTENING
            } else if (nudgeId.toString() != split[0]) {
                lastUsers[dianKey] = "${nudgeId}_1"
            } else {
                lastUsers[dianKey] = "${nudgeId}_${split[1].toInt() + 1}"
            }
        }

        if (event.target.id == 572617659L) {
            if (group.id == 604515343L && Random.nextInt() < 0.5) {
                val image =
                    StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/安害怕.jpg").inputStream)
                group.sendMessage(messageChainOf(PlainText("小路别看福瑞了！"), image))
            }
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
            random < 0.2 -> {
                group.sendMessage(emojiLogic.getRandomImage(group)!!)
            }
            // 随机表情
            random < 0.5 -> {
                val memoryList = JSONObject.parseArray(ClassPathResource("memory.json").file.readLines()
                    .joinToString(separator = ""), Memory::class.java)
                val saying = memoryList[Random.nextInt(0, memoryList.size)].saying
                group.sendMessage(PlainText(saying[Random.nextInt(0, saying.size)]))
            }
            random < 0.7 -> {
                val path = "${FileUtil.localStaticPath}${File.separator}osu"
                val randomFile = FileUtil.getRandomFile(path, "m4a")
                val silkFile = voiceUtil.convertToSilk(randomFile)
                group.sendMessage(StreamMessageUtil.generateAudio(group,
                    File(silkFile).inputStream()))
            }
//            // 来点setu
//            random < 0.6 -> {
//                setuLogic.getRollSetu("", 1, 0, group)
//            }
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