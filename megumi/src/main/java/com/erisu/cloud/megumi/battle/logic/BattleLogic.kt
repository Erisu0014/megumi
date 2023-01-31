package com.erisu.cloud.megumi.battle.logic

import com.erisu.cloud.megumi.util.RedisUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description battleLogic(easy ver.)
 *@Author alice
 *@Date 2023/1/30 11:26
 **/
@Component
class BattleLogic {
    fun checkUid(uid: String): String {
       val sbMember= hashSetOf("701901947","558392851","899659392","620563449","123724626","128821742",
           "714123195","219504657","857586553","117461384","149590383","402771658","553234865")
        return if (sbMember.contains(uid)){
            "uid校验失败，你还是自己讲下你的过去吧"
        }else{
            "uid校验通过[21年12月-22年2月]"
        }
    }

    @Resource
    private lateinit var redisUtil: RedisUtil
//    fun orderBoss(groupId: String, sender: User, order: String) {
//        val orderType = when (order) {
//            "一" -> 1
//            "二" -> 2
//            "三" -> 3
//            "四" -> 4
//            "五" -> 5
//            else -> order.toInt()
//        }
//        BossInfo.values().filter { it.order=orderType }
//    }

//    fun inBoss()

}