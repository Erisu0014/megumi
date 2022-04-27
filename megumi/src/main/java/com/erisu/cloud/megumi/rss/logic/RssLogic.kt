package com.erisu.cloud.megumi.rss.logic

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.erisu.cloud.megumi.rss.mapper.RssMapper
import com.erisu.cloud.megumi.rss.pojo.RssPrefix
import com.erisu.cloud.megumi.rss.pojo.RssSubscription
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource

/**
 *@Description rss相关
 *@Author alice
 *@Date 2021/7/23 13:32
 **/
@Component
open class RssLogic {
    @Value("\${basicUrl}")
    private lateinit var basicUrl: String

    @Resource
    private lateinit var rssMapper: RssMapper

    @Transactional(rollbackFor = [Exception::class])
    open fun subscribe(groupId: String, type: String, uid: String, nickname: String?): Message {
        val rssPrefix = RssPrefix.values().find { it.tag == type } ?: throw Exception("程序异常，prefix不存在")
        val rssSubscription =
            RssSubscription(null, groupId,
                "$basicUrl:1200${rssPrefix.url}$uid", nickname, type)
        val wrapper = QueryWrapper<RssSubscription>()
        wrapper.eq("group_id", rssSubscription.groupId)
            .eq("rss_url", rssSubscription.rssUrl)
            .eq("type", rssSubscription.type)
        if (rssMapper.selectCount(wrapper) > 0) {
            return PlainText("该订阅已存在~不可以重复订阅")
        }
        if (rssMapper.insert(rssSubscription) > 0) return PlainText("订阅成功")
        throw Exception("订阅失败")
    }


    fun getSubscription(groupId: String?): MutableList<RssSubscription> {
        val wrapper = QueryWrapper<RssSubscription>()
        if (groupId != null) wrapper.eq("group_id", groupId)
        return rssMapper.selectList(wrapper)
    }

    fun unSubscribeBilibili(groupId: String, uid: String): Message {
        val wrapper = QueryWrapper<RssSubscription>()
        wrapper.eq("group_id", groupId).eq("rss_url", "$basicUrl:1200${RssPrefix.BILIBILI_DYNAMIC.url}$uid")
        if (rssMapper.delete(wrapper) > 0) return PlainText("取消订阅成功")
        throw Exception("取消订阅失败")
    }

    fun unSubscribeWeibo(groupId: String, uid: String): Message {
        val wrapper = QueryWrapper<RssSubscription>()
        wrapper.eq("group_id", groupId).eq("rss_url", "$basicUrl:1200${RssPrefix.WEIBO_USER.url}$uid")
        if (rssMapper.delete(wrapper) > 0) return PlainText("取消订阅成功")
        throw Exception("取消订阅失败")
    }

    fun preProcess(content: String): Pair<String, String?>? {
        val uid: String
        var nickname: String? = null
        if (content.contains(" ")) {
            val split = content.split(" ")
            uid = split[0]
            nickname = split[1]
        } else uid = content
        return if (uid.toDoubleOrNull() == null) null
        else Pair(uid, nickname)
    }
}