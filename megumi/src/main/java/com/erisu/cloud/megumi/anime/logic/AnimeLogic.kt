package com.erisu.cloud.megumi.anime.logic

import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.anime.pojo.Anime
import com.erisu.cloud.megumi.util.FileUtil
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component
import java.io.File
import kotlin.random.Random

/**
 *@Description TODO
 *@Author alice
 *@Date 2022/8/16 11:07
 **/
@Component
class AnimeLogic {
    fun getRandomMemories(): Message {
        val path = "${FileUtil.localStaticPath}${File.separator}anime${File.separator}memories.json"
        val memories = File(path).readLines().joinToString(separator = "")
        val animeList = JSON.parseArray(memories, Anime::class.java)
        val result = animeList[Random.nextInt(0, animeList.size)].info
        return PlainText(result)
    }
}