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
        val imgTags = Regex("""<img (style )?src="(.+?)".*?>""").findAll(text)
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
        var result = text.removeSurrounding("<![CDATA[ ", " ]]>").trim()
        result = Regex("""&nbsp;""").replace(result, "\n")
        result = Regex("""<img(.*?)src="(.+?)">""").replace(result, "")
        result = Regex("""<iframe src="(.+?)"></iframe>""").replace(result, "")
        result = Regex("""<h2 class="heading">""").replace(result, "\n")
        result = Regex("""<h3 class="subheading">""").replace(result, "\n")
        result = Regex("""</h[1-9]>""").replace(result, "\n")
        result = Regex("""<span.+?">""").replace(result, "")
        result = Regex("""</span>""").replace(result, "")
        result = Regex("""</?strong>""").replace(result, "")
        result = Regex("""</?figure>""").replace(result, "")
        result = Regex("""</div>""").replace(result, "\n")
        result = Regex("""<div.+?>""").replace(result, "")
        result = Regex("""<br.+?>""").replace(result, "\n")
        result = Regex("""</?blockquote>""").replace(result, "\n")
        while (result.endsWith("\n")) {
            result = result.removeSuffix("\n")
        }
        return result
    }

    fun parseVideo(title: String, description: String): String {
        var result = ""
        result += title.removeSurrounding("<![CDATA[ ", " ]]>").trim()
        val videoUrl = Regex("""视频地址：(.+?)<br>""").find(description)!!.groupValues[1]
        result += "\n视频地址：$videoUrl"
        return result
    }
}