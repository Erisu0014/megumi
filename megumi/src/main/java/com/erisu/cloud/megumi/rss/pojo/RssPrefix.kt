package com.erisu.cloud.megumi.rss.pojo

/**
 *@Description rss频道prefix
 *@Author alice
 *@Date 2021/7/30 16:47
 **/
enum class RssPrefix(val tag: String, val url: String) {
    BILIBILI_DYNAMIC("up主", "/bilibili/user/dynamic/"),
    WEIBO_USER("微博", "/weibo/user/")

}