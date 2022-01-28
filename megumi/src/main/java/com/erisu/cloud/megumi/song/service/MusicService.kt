package com.erisu.cloud.megumi.song.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.song.logic.MusicLogic
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description 来点歌
 *@Author alice
 *@Date 2021/7/20 18:09
 **/
@Component
@Model(name = "song")
class MusicService {
    @Resource
    private lateinit var musicLogic: MusicLogic

    @MiraiExperimentalApi
    @Command(commandType = CommandType.GROUP, value = "点歌", pattern = Pattern.PREFIX)
    @Throws(Exception::class)
    fun getMusic(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val keywords = messageChain.contentToString().removePrefix("点歌").trim()
        return musicLogic.getMusic(1, keywords)?.first?:PlainText("没查到结果的说~\uD83D\uDE34")
    }


    @MiraiExperimentalApi
    @Command(commandType = CommandType.GROUP, value = "查歌词", pattern = Pattern.PREFIX)
    @Throws(Exception::class)
    suspend fun getMusicByLyric(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val keywords = messageChain.contentToString().removePrefix("查歌词").trim()
        val pair = musicLogic.getMusic(1006, keywords)
        subject.sendMessage(PlainText(pair!!.second!!))
        return pair.first
    }



}