package com.erisu.cloud.megumi.battle.pojo

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/1/30 16:51
 **/
enum class BossInfo(val order: Int, val key: String,val chinese:String) {
    FIRST(1, "boss:first","一"), SECOND(2, "boss:second","二"),
    THIRD(3, "boss:third","三"), FOURTH(4, "boss:fourth","四"), FIFTH(5, "boss:fifth","五")
}