package com.erisu.cloud.megumi.rss.logic

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.erisu.cloud.megumi.rss.mapper.RssMapper
import com.erisu.cloud.megumi.rss.pojo.RssPrefix
import com.erisu.cloud.megumi.rss.pojo.RssSubscription
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description rss相关
 *@Author alice
 *@Date 2021/7/23 13:32
 **/
@Component
class RssLogic {
    @Value("\${basicUrl}")
    private lateinit var basicUrl: String

    @Resource
    private lateinit var rssMapper: RssMapper

    fun subscribeBilibili(groupId: String, uid: String, nickname: String?): Message {
        val rssSubscription =
            RssSubscription(null, groupId,
                "$basicUrl:1200${RssPrefix.BILIBILI_DYNAMIC.url}$uid", nickname)
        if (rssMapper.insert(rssSubscription) > 0) return PlainText("订阅成功")
        throw Exception("订阅失败")
    }


    fun getSubscription( groupId: String?): MutableList<RssSubscription> {
        val wrapper = QueryWrapper<RssSubscription>()
        if (groupId!=null)wrapper.eq("group_id",groupId)
        return rssMapper.selectList(wrapper)
    }

    fun unSubscribeBilibili(groupId: String, uid: String): Message {
        val wrapper = QueryWrapper<RssSubscription>()
        wrapper.eq("group_id", groupId).eq("rss_url", "$basicUrl:1200${RssPrefix.BILIBILI_DYNAMIC.url}$uid")
        if (rssMapper.delete(wrapper) > 0) return PlainText("取消订阅成功")
        throw Exception("取消订阅失败")
    }
}