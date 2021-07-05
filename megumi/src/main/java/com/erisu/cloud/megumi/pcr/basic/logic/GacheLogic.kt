package com.erisu.cloud.megumi.pcr.basic.logic

import cn.hutool.core.lang.UUID
import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrGache
import com.erisu.cloud.megumi.pcr.basic.pojo.RollResult
import com.erisu.cloud.megumi.pcr.basic.util.GachaFormat
import com.erisu.cloud.megumi.util.*
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import java.io.File
import java.nio.charset.StandardCharsets
import javax.annotation.Resource
import kotlin.random.Random

/**
 *@Description 卡池相关
 *@Author alice
 *@Date 2021/6/24 10:18
 **/
@Component
class GacheLogic {

    @Resource
    private lateinit var avatarMapper: PcrAvatarMapper

    @Resource
    private lateinit var pcrInitData: PcrInitData

    @Resource
    private lateinit var redisUtil: RedisUtil

    suspend fun updateUser(sender: User, messageChain: MessageChain, group: Group): Message {
        var json = ""
        val gachePath = FileUtil.downloadHttpUrl("https://api.redive.lolikon.icu/gacha/default_gacha.json",
            "static", null, "gache.json")
//        val gacheFile = File("static${File.separator}gache.json")
        gachePath.toFile().readLines(StandardCharsets.UTF_8).forEach { json += it }
        val avatarList: MutableList<String> = mutableListOf()
        File("avatar").listFiles()!!.forEach { avatarList.add(it.name) }
        val gache = JSON.parseObject(json, PcrGache::class.java)
        val loseCharacterSet: MutableSet<Int> = mutableSetOf()
        // 日服
        gache.ALL.star1.forEach { if (!avatarList.contains("${it}11.png")) loseCharacterSet.add(it) }
        gache.ALL.star2.forEach { if (!avatarList.contains("${it}11.png")) loseCharacterSet.add(it) }
        gache.ALL.star3.forEach { if (!avatarList.contains("${it}11.png")) loseCharacterSet.add(it) }
        loseCharacterSet.forEach {
            FileUtil.downloadHttpUrl("https://redive.estertion.win/icon/unit/${it}31.webp", "avatar",
                "png", null)
        }
        pcrInitData.gacheJson = json
        return messageChainOf(PlainText("缺失角色:$loseCharacterSet"))
    }


    fun getGache(sender: User, messageChain: MessageChain, group: Group): Message {
//        //  我的回合，抽卡
        var gacheName = redisUtil.get("${RedisKey.GACHE.key}:${group.id}")
        if (gacheName == null) gacheName = "JP"
        val gache = JSON.parseObject(pcrInitData.gacheJson, PcrGache::class.java)
        val rollResult: RollResult = when (gacheName) {
            "JP" -> getMaxRoll(gache.JP, 200)
            "TW" -> getMaxRoll(gache.TW, 300)
            "BL" -> getMaxRoll(gache.BL, 300)
            else -> getMaxRoll(gache.ALL, 300)
        }
        var pyParam = ""
        val avatarPath = "${System.getProperty("user.dir")}${File.separator}avatar"
        val cachePath = "${System.getProperty("user.dir")}${File.separator}cache"
        rollResult.result.forEach { pyParam += "${it}31.png," }
        pyParam = pyParam.removeSuffix(",")
        val fastUUID = UUID.fastUUID().toString(true)
        PythonRunner.runPythonScript(
            "${System.getProperty("user.dir")}${File.separator}script${File.separator}splice_pic.py",
            arrayOf(pyParam, avatarPath, cachePath, fastUUID)
        )
        val image = MessageUtil.generateImageAsync(
            group,
            File("${cachePath}${File.separator}${fastUUID}.png"), true
        ).get()
        return if (rollResult.firstUp != 201) {
            GachaFormat.dog(rollResult, image)
        } else {
            GachaFormat.cat(rollResult, image)
        }
    }

    fun getMaxRoll(gache: PcrGache.SingleGache, num: Int): RollResult {
        var i = 1
        val rollResult = RollResult()
//        var result: MutableList<Int> = mutableListOf()
//        var s3_num = 0
//        var s2_num = 0
//        var s1_num = 0
//        var reward = 0
        while (i <= num) {
            rollResult.reward += when (Random.nextInt(1000)) {
                in 0..gache.up_prob -> {
                    rollResult.result.add(gache.up[Random.nextInt(gache.up.size)])
                    rollResult.s3_num++
                    if (rollResult.firstUp == 201) rollResult.firstUp = i
                    rollResult.memoryChip += 100
                    50
                }
                in gache.up_prob + 1..gache.s3_prob -> {
                    var c_num: Int
                    do {
                        c_num = Random.nextInt(gache.star3.size - gache.up.size)
                    } while (gache.up.contains(c_num))
                    rollResult.result.add(gache.star3[c_num])
                    rollResult.s3_num++
                    50
                }
                in gache.s3_prob + 1..gache.s2_prob -> {
                    // 2星不许up
//                    result.add(gache.star2[Random.nextInt(gache.star2.size)])
                    rollResult.s2_num++
                    10
                }
                else -> {
//                    result.add(gache.star1[Random.nextInt(gache.star1.size)])
                    rollResult.s1_num++
                    1
                }
            }
            i++
        }
        // TODO: 2021/7/2 判断firstUp=0的情况
        return rollResult
    }
}
