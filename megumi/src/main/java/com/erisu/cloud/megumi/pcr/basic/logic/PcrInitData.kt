package com.erisu.cloud.megumi.pcr.basic.logic

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrAvatar
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrPrincess
import org.springframework.util.ResourceUtils


object PcrInitData {
    val nameMap: MutableMap<String, String> = mutableMapOf()
    val idMap: MutableMap<String, List<String>> = mutableMapOf()
    val princessMap: MutableMap<String, PcrPrincess> = mutableMapOf()
    private var isMemoryInited: Boolean = false
    private var isDatabaseInited: Boolean = false


    @Synchronized
    fun initData() {
        if (!isMemoryInited) {
            val characterFile = ResourceUtils.getFile("classpath:basic/character.json")
            // 1.初始化character和反向character
            val line = characterFile.readLines()
            var s = ""
            line.forEach { s += it }
            val json = JSONObject.parseObject(s)
            json.forEach { t, u ->
                val temp = u as JSONArray
                val l = mutableListOf<String>()
                temp.forEach {
                    nameMap[it.toString()] = t
                    l.add(it.toString())
                }
                idMap[t] = l
            }

            // 2.初始化profile
            val profileFile = ResourceUtils.getFile("classpath:basic/profile.json")
            val line1 = profileFile.readLines()
            var s1 = ""
            line1.forEach { s1 += it }
            val json1 = JSONObject.parseObject(s1)
            json1.forEach { t, u ->
                val temp = u as JSONObject
                val profile = PcrPrincess(
                    t, temp.getString("名字"), temp.getString("公会"), temp.getString("生日"),
                    temp.getString("年龄"), temp.getString("身高"), temp.getString("体重"),
                    temp.getString("血型"), temp.getString("种族"), temp.getString("喜好"), temp.getString("声优")
                )
                princessMap[t] = profile
            }
            isMemoryInited = true

        }
    }

    @Synchronized
    fun getAvatarList(baseUrl: String): MutableList<PcrAvatar>? {
        if (!isMemoryInited) {
            initData()
        }
        if (!isDatabaseInited) {
            val avatarList: MutableList<PcrAvatar> = arrayListOf()
            val avatarFile = ResourceUtils.getFile("classpath:basic/character.txt")
            val line = avatarFile.readLines()
            line.forEach { l ->
                val s1 = l.split("\t")
                val s2 = s1[0].split("★")
                val avatar = PcrAvatar(s1[1], nameMap[s2[1]], s2[0].toInt(), "${baseUrl}/${s1[1]}.png")
                avatarList.add(avatar)
//                println("id:${nameMap[s2[1]]},star:${s2[0]},name:${s2[1]},avatar_id:${s1[1]}")
            }
            isDatabaseInited = true
            return avatarList
        }
        return null
    }
}

fun main() {
//    PcrInitData.getAvatarList()
}

