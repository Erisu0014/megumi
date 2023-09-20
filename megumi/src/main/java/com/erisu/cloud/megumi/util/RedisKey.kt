package com.erisu.cloud.megumi.util

/**
 *@Description TODO
 *@Author alice
 *@Date 2021/6/23 8:33
 **/
enum class RedisKey(val key: String) {
    PRINCESS_NAME("princess:name"), PLUGIN("plugin"),GACHE("gacha"),PIXIV("pixiv"),BILIBILI("bilibili"),BOSS_ORDER("boss:order"),
    BOSS_IN("boss:in"),OSU_BG("osu:bg"),OSU_BG_WINNER("osu:bg:winner")
}