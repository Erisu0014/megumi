package com.erisu.cloud.megumi.battle.logic

import cn.hutool.core.date.DateUtil
import com.alibaba.fastjson.JSON
import com.erisu.cloud.megumi.battle.pojo.BossInfo
import com.erisu.cloud.megumi.battle.pojo.OrderInfo
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.User
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
            "uid校验失败，你还是自己讲下你的过去吧"
        } else {
            "uid校验通过[21年12月-22年2月]"
        }
    }


    fun orderBoss(groupId: String, sender: User, order: String): String {
        val orderType = when (order) {
            "一" -> 1
            "二" -> 2
            "三" -> 3
            "四" -> 4
            "五" -> 5
            else -> order.toInt()
        }
        val bossInfo = BossInfo.values().firstOrNull { it.order == orderType }
        if (bossInfo != null) {
            val key = """${RedisKey.BOSS.key}:$groupId"""
            val value = redisUtil.hGet(key, bossInfo.key)
            val orderInfo = OrderInfo(sender.id, DateUtil.formatDate(Date()))
            if (!value.isNullOrBlank()) {
                val orderInfoList = Json.decodeFromString<List<OrderInfo>>(value).toMutableList()
                orderInfoList.replaceAll { if (sender.id == it.userId) orderInfo else it }
                orderInfoList.add(orderInfo)
                redisUtil.hPut(key, bossInfo.key, JSON.toJSONString(orderInfoList))
            } else {
                val orderInfoList = mutableListOf(orderInfo)
                redisUtil.hPut(key, bossInfo.key, JSON.toJSONString(orderInfoList))
            }
        }
        return "预约成功喵"
    }

    fun inBoss() {

    }

}