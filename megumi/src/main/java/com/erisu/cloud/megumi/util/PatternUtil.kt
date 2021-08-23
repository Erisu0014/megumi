package com.erisu.cloud.megumi.util

/**
 *@Description 用以正则判断
 *@Author alice
 *@Date 2021/6/18 15:29
 **/
object PatternUtil {
    fun checkRemoteImage(content: String): String? {
        val pattern = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|].(jpg|png|gif)"
        val regex = Regex(pattern)
        return regex.find(content)?.value
    }

    fun checkRemoteAudio(content: String): String? {
        val pattern = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|].(m4a|amr)"
        val regex = Regex(pattern)
        return regex.find(content)?.value
    }

    fun checkCharacter(content: String): String? {
        val pattern = "谁的(.+)是(.+)"
        val regex = Regex(pattern)
        return regex.find(content)?.value
    }

//    fun checkImg(content: String): MutableList<String>? {
//        val imgTags = Regex("""<img src="(.+?)">""").findAll(content)
//        val imgPattern = """(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|].(jpg|png|gif)"""
//        val imgRegex = Regex(imgPattern)
//        val result:MutableList<String> = mutableListOf()
//        for (tag in imgTags) {
//            val value = imgRegex.find(tag.value)!!.value
//            result.add(value)
//        }
//        return result
//    }
}

fun main() {
    print(Regex("""视频地址：(.+?)<br>""").find("<![CDATA[ #音乐游戏##音游##OSU##osu##osu!##Akatsuki##RX#<br><br>视频地址：https://www.bilibili.com/video/av974552008<br><br><iframe src=\"https://player.bilibili.com/player.html?aid=974552008&high_quality=1\" width=\"650\" height=\"477\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"></iframe><br><br><img src=\"https://i2.hdslb.com/bfs/archive/1a8d2ab1a2cdd3bb36dc243e82941ee43e1aeaef.jpg\" referrerpolicy=\"no-referrer\"> ]]>")!!.groupValues[1])
//    val checkCharacter =
//        PatternUtil.checkImg("【公主连结日服】 主线剧情从第一部到第二部第九章已经全部开放浏览啦，即使是未在Normal关卡中解锁的剧情也可直接浏览！ 各位小伙伴也不妨尝试一下“剧情连续阅览功能”，这样可以轻松地将自己融入故事中哦！ 【最新のメインストーリーまで解放中！】 第2部第9章までの各章を、全て解放できます！もちろん、第1部メインストーリーも引き続き解放可能！ プリコネのストーリーをぜひお楽しみください！ 「連続ストーリー閲覧機能」を使えば、物語へのダイブも快適になりますよ♪<br><br><img src=\"https://i0.hdslb.com/bfs/album/a2d84212cfb6ef432c246e615e19e04e6d4bf8c3.jpg\" referrerpolicy=\"no-referrer\">test<img src=\"1\">")
//    print(checkCharacter)
}



