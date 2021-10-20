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

    fun checkTime(timeline: String) {
        timeline.replace(Regex("[0-9]{2}:[0-9]{2}")) { it.groupValues[0]}
    }

    fun checkMid(str: String) {
        Regex("绑定B站 [0-9]{3,8}").matches(str)
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
    PatternUtil.checkMid("绑定B站 114514asdfdsfas")
//    PatternUtil.checkTime("01:30 战斗開始\n" +
//            "01:22 晶\n" +
//            "01:17 姬塔　王晶技能加攻后ub\n" +
//            "01:11 水狼\n" +
//            "01:11 狗\n" +
//            "01:09 姬塔　自动\n" +
//            "01:09 野性狮鹫\n" +
//            "01:05 姬塔\n" +
//            "01:00 晶\n" +
//            "01:00 狗\n" +
//            "01:00 水狼\n" +
//            "01:00 黄骑\n" +
//            "00:54 姬塔\n" +
//            "00:53 野性狮鹫\n" +
//            "00:49 狗　ムーンライト前に(うまくいかない場合1:00黄骑UBやめ)\n" +
//            "00:47 狗　1:00黄骑UBやめた場合0:46に\n" +
//            "00:47 战斗終了　狼も残っているので0:46目標で")
}



