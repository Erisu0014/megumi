package com.erisu.cloud.megumi.bilibili.logic

import cn.hutool.http.HttpRequest
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.bilibili.pojo.VideoResponse
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.TimeUnit
import kotlin.math.pow

/**
 *@Description bilibili查询相关
 *@Author alice
 *@Date 2021/10/15 13:42
 **/
@Component
class BiliSearchLogic : ApplicationRunner {
    val keys = mutableMapOf(
        "1" to "13",
        "2" to "12",
        "3" to "46",
        "4" to "31",
        "5" to "43",
        "6" to "18",
        "7" to "40",
        "8" to "28",
        "9" to "5",
        "A" to "54",
        "B" to "20",
        "C" to "15",
        "D" to "8",
        "E" to "39",
        "F" to "57",
        "G" to "45",
        "H" to "36",
        "J" to "38",
        "K" to "51",
        "L" to "42",
        "M" to "49",
        "N" to "52",
        "P" to "53",
        "Q" to "7",
        "R" to "4",
        "S" to "9",
        "T" to "50",
        "U" to "10",
        "V" to "44",
        "W" to "34",
        "X" to "6",
        "Y" to "25",
        "Z" to "1",
        "a" to "26",
        "b" to "29",
        "c" to "56",
        "d" to "3",
        "e" to "24",
        "f" to "0",
        "g" to "47",
        "h" to "27",
        "i" to "22",
        "j" to "41",
        "k" to "16",
        "m" to "11",
        "n" to "37",
        "o" to "2",
        "p" to "35",
        "q" to "21",
        "r" to "17",
        "s" to "33",
        "t" to "30",
        "u" to "48",
        "v" to "23",
        "w" to "55",
        "x" to "32",
        "y" to "14",
        "z" to "19"
    )
    val opMap: MutableMap<String, String> = mutableMapOf()

    companion object {
        val client =
            OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build()
        val cookies = "b_nut=1675325975; buvid3=483A6BC4-AD7B-BB61-3E1E-F92DDFC1D41575532infoc; innersign=0"
        val headers = mutableMapOf("Cookie" to cookies).toHeaders()
//        fun getUserCookie(): Headers {
//            val response = client.newCall(Request.Builder().url("https://bilibili.com").build()).execute()
//            if (!response.isSuccessful) {
//                throw Exception("response返回异常，错误码:${response.code}")
//            }
//            return response.headers
//        }
    }


    fun searchUser(keyword: String): Triple<String, String, String>? {
        val url =
            "http://api.bilibili.com/x/web-interface/search/type?search_type=bili_user&keyword=${keyword}"
        val response = client.newCall(Request.Builder().url(url).headers(headers).build()).execute()
        if (!response.isSuccessful) {
            throw Exception("response返回异常，错误码:${response.code}")
        }
        val jo = JSONObject.parseObject(response.body!!.string())
        if (jo["code"] == 0) {
            val result = jo.getJSONObject("data").getJSONArray("result")
            if (result != null && result.isNotEmpty()) {
                val uname = result.getJSONObject(0)["uname"].toString()
                val mid = result.getJSONObject(0)["mid"].toString()
                val pic = result.getJSONObject(0)["upic"].toString()
                return Triple(uname, mid, pic)
            }
            // TODO: 2021/10/18
        }
        return null
    }

    fun searchFollow(mid: String): List<String>? {
        val url = "https://account.bilibili.com/api/member/getCardByMid?mid=${mid}"
        val response = client.newCall(Request.Builder().url(url).headers(headers).build()).execute()
        if (!response.isSuccessful) {
            throw Exception("response返回异常，错误码:${response.code}")
        }
        val jo = JSONObject.parseObject(response.body!!.string())
        if (jo["code"] == 0) {
            val attentions = jo.getJSONObject("card")["attentions"]
            val arr = JSONObject.parseArray(attentions.toString(), String::class.java)
            if (arr.isNotEmpty()) {
                return arr
            }
        }
        return null
    }


    fun searchUser(mid: Number): Triple<String, String, String>? {
        val url =
            "http://api.bilibili.com/x/web-interface/card?mid=${mid}"
        val response = client.newCall(Request.Builder().url(url).headers(headers).build()).execute()
        if (!response.isSuccessful) {
            throw Exception("response返回异常，错误码:${response.code}")
        }
        val jo = JSONObject.parseObject(response.body!!.string())
        if (jo["code"] == 0) {
            val result = jo.getJSONObject("data").getJSONObject("card")
            if (result != null) {
                val uname = result.getString("name")
                val mid = result.getString("mid")
                val pic = result.getString("face")
                return Triple(uname, mid, pic)
            }
        }
        return null
    }

    fun checkDD(user: List<String>): MutableList<String>? {
        val path = "${FileUtil.localStaticPath}${File.separator}vtb${File.separator}dump.json"
        val finder = File(path)
        val vtbs = mutableListOf<String>()
        if (finder.exists()) {
            val result = finder.readLines().joinToString(separator = "")
            val map = JSONObject.parseObject(result, Map::class.java)
            map.forEach { if (user.contains(it.key.toString())) vtbs.add(it.value.toString()) }
            return vtbs
        }
        return null
    }

    // 读取vtb.json并dump化
    override fun run(args: ApplicationArguments?) {
        // TODO: 2021/10/18 云端获取
        val path = "${FileUtil.localStaticPath}${File.separator}vtb"
        val jstr = File("${path}${File.separator}list.json").readLines().joinToString(separator = "")
        val vtbs = JSONObject.parseObject(jstr).getJSONArray("vtbs")
        val map: MutableMap<String, String> = mutableMapOf()
        for (i in 0 until vtbs.size) {
            val arr = vtbs.getJSONObject(i).getJSONArray("accounts")
            if (vtbs.getJSONObject(i).getString("type") != "vtuber" || arr.size == 0) {
                continue
            }
            for (j in 0 until arr.size) {
                if (arr.getJSONObject(j).getString("platform") == "bilibili") {
                    val mid = arr.getJSONObject(j).getString("id")
                    val default = vtbs.getJSONObject(i).getJSONObject("name").getString("default")
                    val name = vtbs.getJSONObject(i).getJSONObject("name").getString(default)
                    map[mid] = name
                    break
                }
            }


        }
        val result = JSON.toJSONString(map)
        val resultPath = Paths.get("${path}${File.separator}dump.json")
        val exists = Files.exists(resultPath, LinkOption.NOFOLLOW_LINKS)
        if (exists) Files.delete(resultPath)
        Files.write(
            resultPath,
            result.encodeToByteArray(),
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.WRITE
        )
    }

    fun bv2av(bv: String): String {
        // 1.去除Bv号前的"Bv"字符
        val bvCode = bv.substring(2)
        // 2. 将key对应的value存入一个列表
        val bvNo2 = mutableListOf<Long>()
        bvCode.forEach { keys[it.toString()]?.let { it1 -> bvNo2.add(it1.toLong()) } }
        // 3.对列表中不同位置的数进行 * 58 的x次方的操作
        bvNo2[0] = bvNo2[0] * 58.0.pow(6).toLong()
        bvNo2[1] = bvNo2[1] * 58.0.pow(2).toLong()
        bvNo2[2] = bvNo2[2] * 58.0.pow(4).toLong()
        bvNo2[3] = bvNo2[3] * 58.0.pow(8).toLong()
        bvNo2[4] = bvNo2[4] * 58.0.pow(5).toLong()
        bvNo2[5] = bvNo2[5] * 58.0.pow(9).toLong()
        bvNo2[6] = bvNo2[6] * 58.0.pow(3).toLong()
        bvNo2[7] = bvNo2[7] * 58.0.pow(7).toLong()
        bvNo2[8] = bvNo2[8] * 58.0.pow(1).toLong()
        bvNo2[9] = bvNo2[9] * 58.0.pow(0).toLong()
        // 4.求出这10个数的合减去100618342136696320与177451812进行异或
        return (bvNo2.sum() - 100618342136696320).xor(177451812).toString()
    }

    suspend fun getAvData(group: Group, av: String): Message? {
        val avCode = if (av.length >= 2 && av.substring(0, 2) == "BV") bv2av(av) else av
        val url = "https://api.bilibili.com/x/web-interface/view?aid=$avCode"
        val headerMap =
            mapOf("Content-Type" to "application/x-www-form-urlencoded")
        val response = HttpRequest.get(url).headerMap(headerMap, true).timeout(2000).execute().body()
        val videoResponse = JSONObject.parseObject(response, VideoResponse::class.java)
        if (videoResponse.code != 0 || (videoResponse.code == 0 && videoResponse.data == null)) {
            return PlainText("${videoResponse.message}喵")
        } else {
            val data = videoResponse.data!!
            val stat = data.stat!!
            val pathResponse = FileUtil.downloadHttpUrl(data.pic!!, "cache", null, null) ?: return null
            return if (pathResponse.code == 200) {
                val image = StreamMessageUtil.generateImage(group, pathResponse.path!!.toFile().inputStream())
                val baseInfo: String = if (stat.view!!.toInt() > 100000) {
                    String.format(
                        "%-20s%-20s%-20s\n%-20s%-20s%-20s", "播放:${stat.view}", "弹幕:${stat.danmaku}",
                        "评论:${stat.reply}", "收藏:${stat.favorite}", "硬币:${stat.coin}", "点赞:${stat.like}"
                    )
                } else {
                    String.format(
                        "%-16s%-16s%-16s\n%-16s%-16s%-16s", "播放:${stat.view}", "弹幕:${stat.danmaku}",
                        "评论:${stat.reply}", "收藏:${stat.favorite}", "硬币:${stat.coin}", "点赞:${stat.like}"
                    )
                }
                messageChainOf(
                    PlainText("${data.title}\n"), image,
                    PlainText("${baseInfo}\n点击链接进入:https://www.bilibili.com/video/av${avCode}\n简介:${data.desc}")
                )
            } else null
        }
    }

    fun getOriginalLink(keyword: String): String? {
        val response = HttpRequest.head("https://b23.tv/$keyword").timeout(2000).execute()
        if (response.status in 200..399) {
            val links = response.header("location")
            val finder = Regex("bilibili.com/video/BV(1[A-Za-z0-9]{2}4.1.7[A-Za-z0-9]{2})").find(links) ?: return null
            return finder.groupValues[1]
        }
        return null
    }

    fun checkOp(name: String, follow: List<String>): Message {
        if (opMap.isEmpty()) {
            val json =
                File("${FileUtil.localStaticPath}${File.separator}op.json").readLines().joinToString(separator = "")
            JSONObject.parseObject(json).forEach {
                opMap[it.key] = it.value.toString()
            }
        }
        val opResult = mutableListOf<String>()
        return if (follow.isEmpty()) {
            PlainText("未查询到此人关注列表>.<")
        } else {
            follow.forEach {
                if (opMap.containsKey(it)) {
                    opMap[it]?.let { it1 -> opResult.add(it1) }
                }
            }
            if (opResult.isNotEmpty()) {
                PlainText("${name}关注列表有：" + opResult.joinToString(separator = "，"))
            } else {
                PlainText("${name}未关注原神相关用户，如有误判，请联系管理员补充")
            }
        }
    }

}
