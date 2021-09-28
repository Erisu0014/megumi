package com.erisu.cloud.megumi.rss.service

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.rss.logic.RssLogic
import com.erisu.cloud.megumi.rss.logic.RssParser
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
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

    @Resource
    private lateinit var rssLogic: RssLogic

    @Value("\${qq.username}")
    private var username: Long = 0

//    @Command(commandType = CommandType.GROUP,
//        value = "test123",
//        pattern = Pattern.EQUALS,
//        uuid = "ab3d79a0df344cce93a7ad0ff52cf46b")
//    @Throws(Exception::class)
//    fun test(sender: User, messageChain: MessageChain, subject: Contact): Message? {
//        bangumi()
//        return null
//    }

    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(fixedDelay = 360_000)
    fun rssConsumption() {
        GlobalScope.future {
            val subscription = rssLogic.getSubscription(null)
            subscription.forEach {
                val url = "${it.rssUrl}?filter_time=360"
                val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
                val bot = Bot.getInstance(username)
                val groupId = it.groupId.toLong()
                val group = bot.getGroup(groupId) as Group
                val title = feed.title.removeSurrounding("<![CDATA[ ", " ]]>").trim()
                feed.entries.forEach { it1 ->
                    val des = it1.description.value
                    val parseText: String = if (des.contains(Regex("<iframe src=(.*)></iframe>"))) {
                        // 说明为视频信息，取标题信息+视频信息
                        rssParser.parseVideo(it1.title, it1.description.value)
                    } else {
                        rssParser.parseText(des)
                    }
                    val parseImage = rssParser.parseImage(des)
                    val chainBuilder = MessageChainBuilder()
                    chainBuilder.append(PlainText("$title\n- - - - - -\n"))
                    chainBuilder.append(PlainText(parseText))
                    parseImage.forEach { it2 ->
                        println(it2)
                        val path = FileUtil.downloadHttpUrl(it2, "cache", null, null)
                        if (path != null) {
                            val image = StreamMessageUtil.generateImage(group, path.toFile(), true)
                            chainBuilder.append(image)
                        }
                    }
                    group.sendMessage(chainBuilder.build())
                }
            }

        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    fun bangumi() {
        GlobalScope.future {
            val url = "http://1.117.219.198:1200/bangumi/calendar/today"
            val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
            val bot = Bot.getInstance(username)
            val groupId = 604515343L
            val group = bot.getGroup(groupId) as Group
            val title = feed.title.removeSurrounding("<![CDATA[ ", " ]]>").trim()
            feed.entries.forEach { it1 ->
                val des = it1.description.value
                val parseText = rssParser.parseText(it1.title.toString()).split("｜").last()
                val parseImage = rssParser.parseImage(des)
                val chainBuilder = MessageChainBuilder()
                chainBuilder.append(PlainText("$title\n- - - - - -\n"))
                chainBuilder.append(PlainText(parseText))
                parseImage.forEach { it2 ->
                    println(it2)
                    val path = FileUtil.downloadHttpUrl(it2, "cache", null, null)
                    if (path != null) {
                        val image = StreamMessageUtil.generateImage(group, path.toFile(), true)
                        chainBuilder.append(image)
                    }
                }
                group.sendMessage(chainBuilder.build())
            }

        }
    }
}

