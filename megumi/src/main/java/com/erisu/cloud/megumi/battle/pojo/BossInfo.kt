package com.erisu.cloud.megumi.battle.pojo

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/1/30 16:51
 **/
enum class BossInfo(val order: Int, val key: String) {
    FIRST(1, "boss:first"), SECOND(2, "boss:second"),
    THIRD(3, "boss:third"), FOURTH(4, "boss:fourth"), FIFTH(5, "boss:fifth")
}