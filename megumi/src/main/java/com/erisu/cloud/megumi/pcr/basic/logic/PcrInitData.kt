package com.erisu.cloud.megumi.pcr.basic.logic

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.pcr.basic.pojo.Profile
import org.springframework.util.ResourceUtils
import java.util.concurrent.locks.ReentrantLock


object PcrInitData {
    val nameMap: MutableMap<String, String> = mutableMapOf()
    val profileMap: MutableMap<String, Profile> = mutableMapOf()
    private var isInited: Boolean = false
    private var lock = ReentrantLock()

    @Synchronized
    fun initData() {
        if (!isInited) {
            val characterFile = ResourceUtils.getFile("classpath:basic/character.json")
            // 1.初始化character
            val line = characterFile.readLines()
            var s = ""
            line.forEach { s += it }
            val json = JSONObject.parseObject(s)
            json.forEach { t, u ->
                val temp = u as JSONArray
                temp.forEach { nameMap[it.toString()] = t }
            }
            isInited = true;

            // 2.初始化profile
            val profileFile = ResourceUtils.getFile("classpath:basic/profile.json")
            val line1 = profileFile.readLines()
            var s1 = ""
            line1.forEach { s1 += it }
            val json1 = JSONObject.parseObject(s1)
            json1.forEach { t, u ->
                val temp = u as JSONObject
                val profile = Profile(
                    t, temp.getString("名字"), temp.getString("公会"), temp.getString("生日"),
                    temp.getString("年龄"), temp.getString("身高"), temp.getString("体重"),
                    temp.getString("血型"), temp.getString("种族"), temp.getString("喜好"), temp.getString("声优")
                )
                profileMap[t] = profile
            }
        }
    }
}

fun main() {
    PcrInitData.initData()
}

