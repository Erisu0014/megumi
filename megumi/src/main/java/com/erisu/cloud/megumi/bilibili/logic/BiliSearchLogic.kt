package com.erisu.cloud.megumi.bilibili.logic

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.util.FileUtil
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 *@Description bilibili查询相关
 *@Author alice
 *@Date 2021/10/15 13:42
 **/
@Component
class BiliSearchLogic : ApplicationRunner {
    fun searchUser(keyword: String): Triple<String, String, String>? {
        val url =
            "http://api.bilibili.com/x/web-interface/search/type?search_type=bili_user&keyword=${keyword}"
        val res = HttpUtil.get(url)
        val jo = JSONObject.parseObject(res)
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
        val res = HttpUtil.get(url)
        val jo = JSONObject.parseObject(res)
        if (jo["code"] == 0) {
            val attentions = jo.getJSONObject("card")["attentions"]
            val arr = JSONObject.parseArray(attentions.toString(), String::class.java)
            if (arr.isNotEmpty()) {
                return arr
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
        Files.write(resultPath,
            result.encodeToByteArray(),
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.WRITE)
    }
}