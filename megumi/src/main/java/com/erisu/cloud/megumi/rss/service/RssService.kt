package com.erisu.cloud.megumi.rss.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.rss.logic.RssParser
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.MessageUtil
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.io.errors.IOException
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL
import javax.annotation.PostConstruct
import javax.annotation.Resource

/**
 *@Description rss相关
 *@Author alice
 *@Date 2021/7/23 10:42
 **/
@Slf4j
@Component
@Model(name = "rssbusuo")
class RssService {
    @Resource
    private lateinit var rssParser: RssParser

    @Value("\${qq.username}")
    private var username: Long = 0

    @Scheduled(fixedDelay = 1000_000)
    fun aliceTest() {
        GlobalScope.future {
            val url = "http://1.117.219.198:1200/bilibili/user/dynamic/1731293061?filter_time=1000"
            val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
            val bot = Bot.getInstance(username)
            val groupId = 604515343L
            val group = bot.getGroup(groupId) as Group
            feed.entries.forEach {
                val des = it.description.value
                val parseText = rssParser.parseText(des)
                val parseImage = rssParser.parseImage(des)
                val chainBuilder = MessageChainBuilder()
                chainBuilder.append(PlainText(parseText))
                parseImage.forEach { it1 ->
                    val path = FileUtil.downloadHttpUrl(it1, "cache", null, null)
                    if (path != null) {
                        val image = MessageUtil.generateImage(group, path.toFile(), true)
                        chainBuilder.append(image)
                    }
                    group.sendMessage(chainBuilder.build())
                }
            }
        }
    }
}

