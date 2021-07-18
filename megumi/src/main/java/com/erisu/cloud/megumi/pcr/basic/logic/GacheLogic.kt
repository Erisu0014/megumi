package com.erisu.cloud.megumi.pcr.basic.logic

import cn.hutool.core.lang.UUID
import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrGacha
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
class GachaLogic {

    @Resource
    private lateinit var avatarMapper: PcrAvatarMapper

    @Resource
    private lateinit var pcrInitData: PcrInitData

    @Resource
    private lateinit var redisUtil: RedisUtil

    fun updateUser(sender: User, messageChain: MessageChain, group: Group): Message {
        // 查看卡池版本号
        val versionFile =
            File("${System.getProperty("user.dir")}${File.separator}static${File.separator}gacha_ver.json")
        var json = ""
        val gachaPath = FileUtil.downloadHttpUrl("https://api.redive.lolikon.icu/gacha/default_gacha.json",
            "static", null, "gacha.json")
//        val gachaFile = File("static${File.separator}gacha.json")
        gachaPath.toFile().readLines(StandardCharsets.UTF_8).forEach { json += it }
        val avatarList: MutableList<String> = mutableListOf()
        File("avatar").listFiles()!!.forEach { avatarList.add(it.name) }
        val gacha = JSON.parseObject(json, PcrGacha::class.java)
        val loseCharacterSet: MutableSet<Int> = mutableSetOf()
        // 日服
        gacha.ALL.star1.forEach { if (!avatarList.contains("${it}11.png")) loseCharacterSet.add(it) }
        gacha.ALL.star2.forEach { if (!avatarList.contains("${it}11.png")) loseCharacterSet.add(it) }
        gacha.ALL.star3.forEach { if (!avatarList.contains("${it}11.png")) loseCharacterSet.add(it) }
        loseCharacterSet.forEach {
            FileUtil.downloadHttpUrl("https://redive.estertion.win/icon/unit/${it}31.webp", "avatar",
                "png", null)
        }
        pcrInitData.gachaJson = json
        return messageChainOf(PlainText("缺失角色:$loseCharacterSet"))
    }


    fun getGacha(sender: User, messageChain: MessageChain, group: Group): Message {
//        //  我的回合，抽卡
        var gachaName = redisUtil.get("${RedisKey.GACHE.key}:${group.id}")
        if (gachaName == null) gachaName = "JP"
        val gacha = JSON.parseObject(pcrInitData.gachaJson, PcrGacha::class.java)
        val rollResult: RollResult = when (gachaName) {
            "JP" -> getMaxRoll(gacha.JP, 200)
            "TW" -> getMaxRoll(gacha.TW, 300)
            "BL" -> getMaxRoll(gacha.BL, 300)
            else -> getMaxRoll(gacha.ALL, 300)
        }

        val image = spliceCacheImage(rollResult.result, group)

        return if (rollResult.firstUp != 201) {
            GachaFormat.dog(rollResult, image)
        } else {
            GachaFormat.cat(rollResult, image)
        }
    }

    fun spliceCacheImage(characterList: MutableList<Int>, group: Group): Image {
        var pyParam = ""
        characterList.forEach { pyParam += "${it}31.png," }
        pyParam = pyParam.removeSuffix(",")
        val avatarPath = "${System.getProperty("user.dir")}${File.separator}avatar"
        val cachePath = "${System.getProperty("user.dir")}${File.separator}cache"

        val fastUUID = UUID.fastUUID().toString(true)
        PythonRunner.runPythonScript(
            "${System.getProperty("user.dir")}${File.separator}script${File.separator}splice_pic.py",
            arrayOf(pyParam, avatarPath, cachePath, fastUUID)
        )
        return MessageUtil.generateImageAsync(
            group,
            File("${cachePath}${File.separator}${fastUUID}.png"), true
        ).get()
    }

    fun getMaxRoll(gacha: PcrGacha.SingleGacha, num: Int): RollResult {
        var i = 1
        val rollResult = RollResult()
//        var result: MutableList<Int> = mutableListOf()
//        var s3_num = 0
//        var s2_num = 0
//        var s1_num = 0
//        var reward = 0
        while (i <= num) {
            rollResult.reward += when (Random.nextInt(1000)) {
                in 0..gacha.up_prob -> {
                    rollResult.result.add(gacha.up[Random.nextInt(gacha.up.size)])
                    rollResult.s3_num++
                    if (rollResult.firstUp == 201) rollResult.firstUp = i
                    rollResult.memoryChip += 100
                    50
                }
                in gacha.up_prob + 1..gacha.s3_prob -> {
                    var c_num: Int
                    do {
                        c_num = Random.nextInt(gacha.star3.size - gacha.up.size)
                    } while (gacha.up.contains(c_num))
                    rollResult.result.add(gacha.star3[c_num])
                    rollResult.s3_num++
                    50
                }
                in gacha.s3_prob + 1..gacha.s2_prob -> {
                    // 2星不许up
//                    result.add(gacha.star2[Random.nextInt(gacha.star2.size)])
                    rollResult.s2_num++
                    10
                }
                else -> {
//                    result.add(gacha.star1[Random.nextInt(gacha.star1.size)])
                    rollResult.s1_num++
                    1
                }
            }
            i++
        }
        return rollResult
    }
}
