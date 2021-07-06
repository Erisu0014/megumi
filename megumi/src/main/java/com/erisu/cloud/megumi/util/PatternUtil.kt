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
}

fun main() {
    val checkCharacter = PatternUtil.checkCharacter("谁的年龄是12")
    print(checkCharacter)
}



