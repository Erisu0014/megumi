package com.erisu.cloud.megumi.anime.logic

import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.anime.pojo.Anime
import com.erisu.cloud.megumi.util.FileUtil
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component
import java.io.File

/**
 *@Description TODO
 *@Author alice
 *@Date 2022/8/16 11:07
 **/
@Component
class AnimeLogic {
    fun getRandomMemories(from: String?): Message {
        val path = "${FileUtil.localStaticPath}${File.separator}anime${File.separator}memories.json"
        val memories = File(path).readLines().joinToString(separator = "")
        val animeList = JSON.parseArray(memories, Anime::class.java)
        return if (from.isNullOrBlank()) {
            PlainText(animeList.random().info)
        } else {
            val fromUppercase = from.toUpperCase()
            val map = animeList.groupBy { it.from }
            if (map.containsKey(fromUppercase)) {
                PlainText(map[fromUppercase]?.random()!!.info)
            } else {
                PlainText("还没有学会这个动画的句子呢>.<\n随机返回如下\n${animeList.random().info}")
            }
        }

    }
}