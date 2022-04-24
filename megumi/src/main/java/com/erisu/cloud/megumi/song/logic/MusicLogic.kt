package com.erisu.cloud.megumi.song.logic

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.song.pojo.BasicSongResponse
import com.erisu.cloud.megumi.song.pojo.RecommendSongResponse
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.random.Random

/**
 *@Description TODO
 *@Author alice
 *@Date 2021/7/21 14:35
 **/
@Component
class MusicLogic {
    @Value("\${basicUrl}")
    private lateinit var basicUrl: String

    @Value("\${netEasePhone}")
    private lateinit var netEasePhone: String

    @Value("\${netEasePassword}")
    private lateinit var netEasePassword: String

    /**
     *
     */
    fun getMusic(type: Int, keywords: String): Pair<Message, String?>? {
        if (keywords.length > 100) {
            return null
        }
        val response = HttpUtil.get("$basicUrl:3000/cloudsearch?type=$type&keywords=$keywords", 2000)
        val musicResponse = JSONObject.parseObject(response, BasicSongResponse::class.java)
        if (musicResponse.code != 200 || musicResponse.result.songCount < 1) {
            return null
        }
        val song = musicResponse.result.songs[0]
        if (song.ar.joinToString(separator = "/") { it.name } == "嘉然Diana") {
            return Pair(PlainText("你可少看点as吧\uD83D\uDC7F"), null)
        }
        val musicShare = MusicShare(
            kind = MusicKind.NeteaseCloudMusic,
            title = song.name,
            summary = song.ar.joinToString(separator = "/") { it.name },
            brief = "[分享]${song.name}",
            jumpUrl = "https://y.music.163.com/m/song?id=${song.id}",
            pictureUrl = song.al.picUrl,
            musicUrl = "http://music.163.com/song/media/outer/url?id=${song.id}"
        )
        return if (type == 1006) Pair(musicShare, song.lyrics!!.joinToString("\n")) else Pair(musicShare, null)

    }

    fun login(): Boolean {
        val loginStatus = HttpUtil.get("$basicUrl:3000/login/status", 2000)
        val statusObj = JSONObject.parseObject(loginStatus)
        //用户未登录
        if (statusObj["account"] == null || statusObj["code"] != 200) {
            val login =
                HttpUtil.get("$basicUrl:3000/login/cellphone?phone=$netEasePhone&password=$netEasePassword", 2000)
            if (JSONObject.parseObject(login)["code"] != 200) {
                return false
            }
        }
        return true
    }

    fun getRandomSongs(): Message? {
        if (!login()) {
            return null
        }
        val response = HttpUtil.get("$basicUrl:3000/recommend/songs", 2000)
        val musicResponse = JSONObject.parseObject(response, RecommendSongResponse::class.java)
        if (musicResponse.code != 200 || musicResponse.data!!.dailySongs.isEmpty()) {
            return null
        }
        val size = musicResponse.data.dailySongs.size
        val song = musicResponse.data.dailySongs[Random.nextInt(size)]
        return MusicShare(
            kind = MusicKind.NeteaseCloudMusic,
            title = song.name,
            summary = song.ar.joinToString(separator = "/") { it.name },
            brief = "[分享]${song.name}",
            jumpUrl = "https://y.music.163.com/m/song?id=${song.id}",
            pictureUrl = song.al.picUrl,
            musicUrl = "http://music.163.com/song/media/outer/url?id=${song.id}"
        )
    }

}