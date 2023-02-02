package com.erisu.cloud.megumi.battle.logic

import cn.hutool.core.date.DateUtil
import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.battle.pojo.BossInfo
import com.erisu.cloud.megumi.battle.pojo.BossUserInfo
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.Resource

/**
 *@Description battleLogic(easy ver.)
 *@Author alice
 *@Date 2023/1/30 11:26
 **/
@Component
class BattleLogic {
    @Resource
    private lateinit var redisUtil: RedisUtil
    fun checkUid(uid: String): String {
        val sbMember = hashSetOf(
            "701901947", "558392851", "899659392", "620563449", "123724626", "128821742",
            "714123195", "219504657", "857586553", "117461384", "149590383", "402771658", "553234865"
        )
        return if (sbMember.contains(uid)) {
            "诶嘿，uid校验失败"
        } else {
            "uid校验通过[21年12月-22年2月]"
        }
    }


    fun orderBoss(groupId: String, sender: User, order: String): String {
        val bossInfo = getBossInfo(order)
        if (bossInfo != null) {
            val orderKey = """${RedisKey.BOSS_ORDER.key}:$groupId"""
            updateBossDetail(orderKey, bossInfo, sender.id)
        }
        return "预约成功喵"
    }

    fun checkOrderBoss(groupId: String, sender: User, order: String): PlainText? {
        val bossInfo = getBossInfo(order)
        if (bossInfo != null) {
            val result = redisUtil.hGet("""${RedisKey.BOSS_ORDER.key}:$groupId""", bossInfo.key)
            return if (!result.isNullOrBlank()) {
                val bossUserInfoList = Json.decodeFromString<List<BossUserInfo>>(result).toMutableList()
                val result = bossUserInfoList.joinToString { "┗${it.userId} \uD83D\uDD70︎${it.time}\n" }
                PlainText("预约${bossInfo.chinese}王的骑士君：\n${result}")
            } else {
                PlainText("${bossInfo.chinese}王还没有骑士君预约哦")
            }
        }
        return null
    }


    private fun getBossInfo(order: String): BossInfo? {
        val orderType = when (order) {
            BossInfo.FIRST.chinese -> 1
            BossInfo.SECOND.chinese -> 2
            BossInfo.THIRD.chinese -> 3
            BossInfo.FOURTH.chinese -> 4
            BossInfo.FIFTH.chinese -> 5
            else -> order.toInt()
        }
        return BossInfo.values().firstOrNull { it.order == orderType }
    }

    fun inBoss(groupId: String, sender: User, order: String): PlainText? {
        val bossInfo = getBossInfo(order)
        if (bossInfo != null) {
            val orderKey = """${RedisKey.BOSS_ORDER.key}:$groupId"""
            val orderValue = redisUtil.hGet(orderKey, bossInfo.key)
            if (!orderValue.isNullOrBlank()) {
                val bossUserInfoList = Json.decodeFromString<List<BossUserInfo>>(orderValue).toMutableList()
                bossUserInfoList.removeIf { sender.id == it.userId }
                if (bossUserInfoList.isEmpty()){
                    redisUtil.hDelete(orderKey, bossInfo.key)
                }else{
                    redisUtil.hPut(orderKey, bossInfo.key, JSON.toJSONString(bossUserInfoList))
                }
            }
            val inKey = """${RedisKey.BOSS_IN.key}:$groupId"""
            val size = updateBossDetail(inKey, bossInfo, sender.id)
            return PlainText(
                "已登记进本战斗\n" +
                        "┗当前${bossInfo.chinese}王战斗人数：${size}\n"
            )
        } else {
            return null
        }
    }


    private fun updateBossDetail(
        key: String,
        bossInfo: BossInfo,
        userId: Long,
    ): Int {
        val value = redisUtil.hGet(key, bossInfo.key)
        val bossUserInfo = BossUserInfo(userId, DateUtil.formatTime(Date()))
        return if (!value.isNullOrBlank()) {
            val bossUserInfoList = Json.decodeFromString<List<BossUserInfo>>(value).toMutableList()
            bossUserInfoList.replaceAll { if (userId == it.userId) bossUserInfo else it }
            redisUtil.hPut(key, bossInfo.key, JSON.toJSONString(bossUserInfoList))
            bossUserInfoList.size
        } else {
            val orderInfoList = mutableListOf(bossUserInfo)
            redisUtil.hPut(key, bossInfo.key, JSON.toJSONString(orderInfoList))
            1
        }
    }
}