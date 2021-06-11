package com.erisu.cloud.megumi.battle.util

/**
 *@Description 补偿刀，整刀，尾刀
 *@Author alice
 *@Date 2021/6/8 15:50
 **/
enum class DamageType(val typeName: String) {
    Complete("完整刀"), Incomplete("补偿刀"), Last("尾刀")
}