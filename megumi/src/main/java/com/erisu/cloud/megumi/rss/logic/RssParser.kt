package com.erisu.cloud.megumi.rss.logic

import org.springframework.stereotype.Component

/**
 *@Description rss解析器
 *@Author alice
 *@Date 2021/7/23 10:47
 **/
@Component
class RssParser {

    /**
     * 获取文本中的图像信息
     *
     * @return  imageUrl
     */
    fun parseImage(text: String): MutableList<String> {
        val imgTags = Regex("""<img src="(.+?)">""").findAll(text)
        val imgPattern = """(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|].(jpg|png|gif)"""
        val imgRegex = Regex(imgPattern)
        val result: MutableList<String> = mutableListOf()
        for (tag in imgTags) {
            val value = imgRegex.find(tag.value)!!.value
            result.add(value)
        }
        return result
    }

    fun parseText(text: String): String {
        val removeImgStr = Regex("""<img src="(.+?)">""").replace(text, "")
        return Regex("""<br>""").replace(removeImgStr, "\n").removeSuffix("\n")
    }
}