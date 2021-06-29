package com.erisu.cloud.megumi.battle.util

import com.erisu.cloud.megumi.battle.pojo.BattleBoss
import com.erisu.cloud.megumi.battle.pojo.BattleDamage
import com.erisu.cloud.megumi.battle.pojo.DamagedBoss
import com.erisu.cloud.megumi.battle.pojo.NowBoss
import java.util.*

/**
 *@Description battle相关字符处理：单例模式
 *@Author alice
 *@Date 2021/6/4 17:37
 **/
object BattleFormat {
    fun nowBossInfo(round: Int, boss: List<BattleBoss>): String {
        val bosses = boss.sortedBy { it.bossOrder }
        var boss_info = ""
        bosses.forEach { boss_info += "${it.bossOrder}号boss，生命值：${it.hpMax}\n" }
        return "现在${round}周目:\n${boss_info}今天已出0刀，剩余90刀完整刀和0刀补偿刀\n"
    }

    @JvmName("nowBoss1Info")
    fun nowBossInfo(round: Int, boss: List<NowBoss>): String {
        val bosses = boss.sortedBy { it.bossOrder }
        var boss_info = ""
        bosses.forEach { boss_info += "${it.bossOrder}号boss，生命值：${it.hpNow}\n" }
        return "现在${round}周目:\n${boss_info}今天已出0刀，剩余90刀完整刀和0刀补偿刀\n"
    }

    /**
     * 根据order信息获取stage
     */
    fun getStage(order: Int): Int {
        return when (order) {
            in 1..3 -> 1
            in 4..10 -> 2
            in 11..30 -> 3
            in 31..40 -> 4
            else -> 5

        }
    }


    fun fuckBossLastInfo(
        damageType: DamageType,
        qqCard: String,
        boss: DamagedBoss,
        damageTime: Double,
    ): String {
        return "${qqCard}对${boss.nowBoss.bossName}造成了${boss.damage}点伤害，击败了boss\n" +
                "（今日第${3 - damageTime}刀，${damageType.typeName}）\n" +
                "现在${boss.nowBoss.bossRounds}周目，${boss.nowBoss.bossOrder}号boss\n" +
                "生命值${boss.nowBoss.hpNow}"
    }


    fun fuckBossInfo(
        damageType: DamageType,
        qqCard: String,
        boss: DamagedBoss,
        damageTime: Double,
    ): String {
        return "${qqCard}对${boss.nowBoss.bossName}造成了${boss.damage}点伤害\n" +
                "（今日第${3 - damageTime}刀，${damageType.typeName}）\n" +
                "现在${boss.nowBoss.bossRounds}周目，${boss.nowBoss.bossOrder}号boss\n" +
                "生命值${boss.nowBoss.hpNow}"
    }

    fun revertDamageInfo(
        qqCard: String,
        nowBoss: NowBoss,
    ): String {
        return "${qqCard}的出刀记录已被撤销\n" +
                "现在${nowBoss.bossRounds}周目，${nowBoss.bossOrder}号boss\n" +
                "生命值${nowBoss.hpNow}\n";
    }

    fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return date.after(calendar.time)
    }
}
