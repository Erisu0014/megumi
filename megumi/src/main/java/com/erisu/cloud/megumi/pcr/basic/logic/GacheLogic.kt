package com.erisu.cloud.megumi.pcr.basic.logic

import cn.hutool.core.lang.UUID
import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrGache
import com.erisu.cloud.megumi.pcr.basic.pojo.RollResult
import com.erisu.cloud.megumi.util.*
import com.erisu.cloud.megumi.util.MessageUtil.message
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.jsoup.select.Evaluator.Class
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.CompletableFuture
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

    fun updateUser(sender: User, messageChain: MessageChain, group: Group): Message? {
        return null
    }


    suspend fun getGache(sender: User, messageChain: MessageChain, group: Group): Message {
        //  我的回合，抽卡
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
        PythonRunner.runPythonScript("${System.getProperty("user.dir")}${File.separator}script${File.separator}splice_pic.py",
            arrayOf(pyParam, avatarPath, cachePath, fastUUID))
        return messageChainOf(PlainText("素敵な仲間が増えますよ！ \n"),
            MessageUtil.generateImage(group, File("${cachePath}${File.separator}${fastUUID}.png"), true),
            PlainText(
                        "★★★${rollResult.s3_num}× ★★×${rollResult.s2_num} ★×${rollResult.s1_num}\n" +
                        "获得记忆碎片${rollResult.memoryChip}与女神秘石×${rollResult.reward}！\n" +
                        "第${rollResult.firstUp}抽首次获得up角色\n" +
                        "记忆碎片一大堆！您是托吧？\n"))


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
                    if (rollResult.firstUp == 0) rollResult.firstUp = num
                    else rollResult.memoryChip += 100
                    50
                }
                in gache.up_prob + 1..gache.s3_prob -> {
                    var c_num = 0
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

    fun getOneCharacter(all: MutableList<Int>, up: MutableList<Int>, up_prob: Int, all_prob: Int) {
        var trueUp: MutableList<Int> = mutableListOf()
        // 深拷贝
        var copyAll = all.toMutableList()
        up.forEach { if (all.contains(it)) trueUp.add(it) }
//        // 需向all中填充的值 不推荐该做法。对于无穷小数无限扩容会造成内存泄露
//        val num = all.size * up_prob / (all_prob - up_prob)
    }
}
