package com.erisu.cloud.megumi.pcr.basic.logic

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrAvatar
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrPrincess
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import javax.annotation.Resource

@Component
class PcrInitData {
    @Resource
    private lateinit var redisUtil: RedisUtil

    val nameMap: MutableMap<String, String> = mutableMapOf()
    val idMap: MutableMap<String, List<String>> = mutableMapOf()
    val princessMap: MutableMap<String, PcrPrincess> = mutableMapOf()
    var gacheJson: String = ""
    private var isMemoryInited: Boolean = false
    private var isDatabaseInited: Boolean = false


    @Synchronized
    fun initData() {
        if (!isMemoryInited) {
//            val characterFile = ClassPathResource("basic/character.json").inputStream.reader(StandardCharsets.UTF_8)
            val characterFile = File("static${File.separator}character.json")
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
            // 增加redis name
            val nameExtraMap = redisUtil.hGetAll(RedisKey.PRINCESS_NAME.key)
            if (!nameExtraMap.isNullOrEmpty()) nameMap.putAll(nameExtraMap as Map<out String, String>)
            // 2.初始化profile
            val profileFile = ClassPathResource("basic/profile.json").inputStream.reader(StandardCharsets.UTF_8)
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
            // 3.更新卡池信息
            val gachePath = FileUtil.downloadHttpUrl("https://api.redive.lolikon.icu/gacha/default_gacha.json",
                "static", null)
            gachePath.toFile().readLines(StandardCharsets.UTF_8).forEach { gacheJson += it }
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
            val avatarFile = ClassPathResource("basic/character.txt").inputStream.reader(StandardCharsets.UTF_8)
//            val avatarFile = ResourceUtils.getFile("classpath:basic/character.txt")
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

