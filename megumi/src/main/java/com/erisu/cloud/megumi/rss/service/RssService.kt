package com.erisu.cloud.megumi.rss.service

import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateUtil
import cn.hutool.http.HttpRequest
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.rss.logic.RssLogic
import com.erisu.cloud.megumi.rss.logic.RssParser
import com.erisu.cloud.megumi.rss.pojo.RssPrefix
import com.erisu.cloud.megumi.rss.pojo.UmaNews
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*
import javax.annotation.Resource

/**
 *@Description rss相关
 *@Author alice
 *@Date 2021/7/23 10:42
 **/
@Slf4j
@Component
//@Model(name = "rss推送")
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
//    fun test123(sender: User, messageChain: MessageChain, subject: Contact): Message? {
//        umamusume()
//        return null
//    }

    //    @Async
    @Scheduled(fixedDelay = 3600_000)
    fun rssConsumption() {
        GlobalScope.future {
            val subscription = rssLogic.getSubscription(null)
            subscription.forEach {
                val url = "${it.rssUrl}?filter_time=3600"
                val feed: SyndFeed = SyndFeedInput().build(XmlReader(URL(url)))
                val bot = Bot.getInstance(username)
                val groupId = it.groupId.toLong()
                val group = bot.getGroup(groupId) as Group
                when (it.type) {
                    RssPrefix.BILIBILI_DYNAMIC.tag -> {
                        bilibili(group, feed)
                    }
                    RssPrefix.WEIBO_USER.tag -> {
                        weibo(group, feed)
                    }
                }

            }
        }
    }

    suspend fun bilibili(group: Group, feed: SyndFeed) {
        val title = feed.title.removeSurrounding("<![CDATA[ ", " ]]>").trim()
        val forwardMessageBuilder = ForwardMessageBuilder(group)
        feed.entries.forEach {
            val des = it.description.value
            val parseText: String =
                if (des.contains(Regex("<iframe src=(.*)></iframe>"))) {
                    // 说明为视频信息，取标题信息+视频信息
                    rssParser.parseVideo(it.title, it.description.value)
                } else {
                    rssParser.parseText(des)
                }
            val images = rssParser.parseImage(des)
            val chainBuilder = MessageChainBuilder()
            chainBuilder.append(PlainText("$title\n- - - - - -\n"))
            chainBuilder.append(PlainText(parseText))
            FileUtil.buildImages(group, images, chainBuilder)
            forwardMessageBuilder.add(1269732086, "色图bot", chainBuilder.build())
        }
        if (forwardMessageBuilder.size!=0){
//            print(forwardMessageBuilder.build().contentToString())
            group.sendMessage(forwardMessageBuilder.build())
        }

    }


    suspend fun weibo(group: Group, feed: SyndFeed) {
        val title = feed.title.removeSurrounding("<![CDATA[ ", " ]]>").trim()
        val forwardMessageBuilder = ForwardMessageBuilder(group)
        feed.entries.forEach {
            val des = it.description.value
            var parseText: String = rssParser.parseText(des)
            parseText = Regex("""<a href="https://(.*?)weibo.(.+?)/(.+?)">""").replace(parseText, "")
//            parseText = Regex("""<a href="https://weibo.cn/(.+?)</a>""").replace(parseText, "")
//            parseText = Regex("""<a href="https://weibo.com/(.+?)</a>""").replace(parseText, "")
            parseText = Regex("""<a data-url=(.+?)</a>""").replace(parseText, "")
            parseText = Regex("""<a data-url=(.+?)</a>""").replace(parseText, "")
            parseText = parseText.replace("</a>", "")
            parseText = parseText.replace(Regex("\n+"), "\n")
            val videoPicFinder = Regex("""<video(.*?)poster="(.*?)".*?></video>""").find(parseText)
            val images = rssParser.parseImage(des)
            if (videoPicFinder != null) {
                images.add(videoPicFinder.groupValues[2])
            }
            // 删除video标签
            parseText = Regex("""<video(.*?)poster="(.*?)".*?></video>""").replace(parseText, "")
//            val nodes: MutableList<ForwardMessage.Node> = mutableListOf()
            var chainBuilder = MessageChainBuilder()
            chainBuilder.append(PlainText("$title\n- - - - - -\n"))
            chainBuilder.append(PlainText(parseText))
            FileUtil.buildImages(group, images, chainBuilder)
            forwardMessageBuilder.add(1269732086, "色图bot", chainBuilder.build())
        }
        if(forwardMessageBuilder.size!=0){
//            print(forwardMessageBuilder.build().contentToString())
            group.sendMessage(forwardMessageBuilder.build())
        }
    }


    //    @Scheduled(cron = "0 0 9 * * ?")
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
                    val pathResponse = FileUtil.downloadHttpUrl(it2, "cache", null, null)
                    if (pathResponse != null && pathResponse.code == 200) {
                        val image = StreamMessageUtil.generateImage(group, pathResponse.path!!.toFile(), true)
                        chainBuilder.append(image)
                    }
                }
                group.sendMessage(chainBuilder.build())
            }

        }
    }

    //    @OptIn(DelicateCoroutinesApi::class)
//    @Scheduled(fixedDelay = 360_000)
    fun umamusume() {
        GlobalScope.future {
            val bot = Bot.getInstance(username)
            val groupId = 604515343L
            val group = bot.getGroup(groupId) as Group
            val url = "https://umamusume.jp/api/ajax/pr_info_index?format=json"
            val headerMap =
                mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36 Edg/89.0.774.50",
                    "Accept" to "application/json")
            val requestMap = mapOf("announce_label" to 0, "limit" to 10, "offset" to 0)
            val res = HttpRequest.post(url).headerMap(headerMap, true).timeout(2000).body(JSON.toJSONString(requestMap))
                .execute().body()
            val resPo = JSONObject.parseObject(res)
            if (resPo["response_code"] == 1) {
                val umaNews =
                    JSONObject.parseArray(JSON.toJSONString(resPo["information_list"]), UmaNews::class.java)
                umaNews.forEach {
                    if (DateUtil.parseDateTime(it.post_at) > DateUtil.offset(Date(), DateField.MINUTE, -6)) {
                        val text = rssParser.parseText(it.message)
                        val pics = rssParser.parseImage(it.message)
                        val chainBuilder = MessageChainBuilder()
                        chainBuilder.append(PlainText("@ウマ娘プリティーダービー\n【${it.title}】\n${text}"))
                        FileUtil.buildImages(group, pics, chainBuilder)
                        group.sendMessage(chainBuilder.build())
                    }
                }
            }
        }
    }


}

