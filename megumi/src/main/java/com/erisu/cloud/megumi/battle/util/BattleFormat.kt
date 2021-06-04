package com.erisu.cloud.megumi.battle.util

import com.erisu.cloud.megumi.battle.pojo.BattleBoss
import com.erisu.cloud.megumi.battle.pojo.NowBoss

/**
 *@Description battle相关字符处理：单例模式
 *@Author alice
 *@Date 2021/6/4 17:37
 **/
object BattleFormat {
    fun nowBoss(round: Int, boss: List<BattleBoss>): String {
        val bosses = boss.sortedBy { it.bossOrder }
        var boss_info = "";
        bosses.forEach { boss_info += "${it.bossOrder}号boss，生命值：${it.hpMax}\n" }
        return "现在${round}周目:\n${boss_info}今天已出0刀，剩余90刀完整刀和0刀补偿刀\n"
    }

    @JvmName("nowBoss1")
    fun nowBoss(round: Int, boss: List<NowBoss>): String {
        val bosses = boss.sortedBy { it.bossOrder }
        var boss_info = ""
        bosses.forEach { boss_info += "${it.bossOrder}号boss，生命值：${it.hpNow}\n" }
        return "现在${round}周目:\n${boss_info}今天已出0刀，剩余90刀完整刀和0刀补偿刀\n"
    }
}
