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
        timeline.replace(Regex("[0-9]{2}:[0-9]{2}")) { it.groupValues[0] }
    }

    fun checkMid(str: String) {
        Regex("绑定B站 [0-9]{3,8}").matches(str)
    }

    fun blueArchive(str: String) {
        val regex =
        Regex("<td class=\"student-name\">.+?<div class=\"locales\" data-cn=\"(.+?)\" data-jp=\"(.+?)\".*?data-tw=\"(.+?)\".+?data-en=\"(.+?)\">.+?</div></a></td>",
            RegexOption.DOT_MATCHES_ALL)
//        val regex =
//            Regex("<td class=\"student-name\">.+?<div (.+?)</div></a></td>",
//                RegexOption.DOT_MATCHES_ALL)
        val iterator = regex.findAll(str).iterator()
        val regex2=Regex("<a class=\"img-link\".+?<img class=\"img-student.+?data-src=\"(.+?)\" width=\"120px\".+?></a>")
        val iterator2 = regex2.findAll(str).iterator()
        var i=0
        while (iterator.hasNext()&&iterator2.hasNext()) {
            val name = iterator.next().groupValues.drop(1).joinToString(",") { "\"$it\"" }
            val pic = iterator2.next().groupValues.drop(1).joinToString(",")
            print((5500+i).toString()+":[$name],\n")
//            FileUtil.downloadHttpUrl(pic,"ba","png","icon_unit_${5500+i}31")
            i++
        }


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

fun main(){
}





