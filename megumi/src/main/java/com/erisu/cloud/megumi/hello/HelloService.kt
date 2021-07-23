package com.erisu.cloud.megumi.hello

import com.erisu.cloud.megumi.battle.util.MarsUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.MessageModel
import com.erisu.cloud.megumi.util.MessageUtil
import com.erisu.cloud.megumi.util.PatternUtil.checkRemoteAudio
import com.erisu.cloud.megumi.util.PatternUtil.checkRemoteImage
import com.erisu.cloud.megumi.util.RedisUtil
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.FileNotFoundException
import java.net.URL
import javax.annotation.Resource


/**
 * @Description hello world girl
 * @Author alice
 * @Date 2020/12/10 16:25
 */
@Slf4j
@Component
@Model(name = "hello")
class HelloService {
    @Resource
    private lateinit var marsUtil: MarsUtil

    @Resource
    private lateinit var redisUtil: RedisUtil

    @Command(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.EQUALS, alias = ["zaima", "zai"])
    @Throws(Exception::class)
    fun hello(sender: User, messageChain: MessageChain, subject: Contact): Message {
        return PlainText("はい！私はいつも貴方の側にいるよ～")
    }


    @Command(commandType = CommandType.GROUP, value = "(.+)(？|\\?)", pattern = Pattern.REGEX)
    @Throws(Exception::class)
    fun test(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val forward: ForwardMessage = buildForwardMessage(subject) {
            add(1503306252, "xcw", PlainText("不一定"))
//            add(sender, PlainText("你说得对"))
//            add(sender) {
//                // this: MessageChainBuilder
//                add(At(sender.id))
//                add(PlainText("msg"))
//            }
//            add(event) // 可添加 MessageEvent
        }
        return forward
    }

    @MiraiExperimentalApi
    @Command(commandType = CommandType.GROUP, value = "test2", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    fun test2(sender: User, messageChain: MessageChain, subject: Contact): Message {
        return buildXmlMessage(1) {
            flag = 0
            brief = "色xcw"
            action = "web"
            url = "https://redive.estertion.win/sound/unit_battle_voice/111101/vo_btl_111101_ub_200.m4a"
            item {
                layout = 2
                title("xcw已加入该会话")
                picture("http://gchat.qpic.cn/gchatpic_new/1269732086/826119271-2371198257-766C3EAF0B7DDE7AE876BBF5171BA325/0")
                summary("xcw已开始监控聊天")
            }
            source("不许发xcw色图")
        }

    }

//    @MiraiInternalApi
//    @Command(commandType = CommandType.GROUP, pattern = Pattern.CHECK_IMAGE)
//    @Throws(Exception::class)
//    suspend fun test3(sender: User, messageChain: MessageChain, subject: Contact): Message? {
//        val i: GroupImage = messageChain[1] as GroupImage
////        print(i.queryUrl())
//        return null
//    }


    @Command(commandType = CommandType.GROUP, value = "谁是曲奇", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun shigetora(sender: User, messageChain: MessageChain, group: Group): Message {
        return MessageUtil.generateAudio(group, ClassPathResource("shigetora.m4a").inputStream, "m4a")
    }

    @Command(commandType = CommandType.GROUP, value = "切换图片模式", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    fun changeModel(sender: User, messageChain: MessageChain, group: Group): Message {
        redisUtil.set("model", MessageModel.IMAGE.type.toString())
        return PlainText("切换成功")
    }

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS, uuid = "6cbd28fa91f0469698dff603d8635fca")
    @Throws(Exception::class)
    suspend fun showImage(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val content = messageChain.contentToString()
        val imageUrl = checkRemoteImage(content) ?: return null
        val path = FileUtil.downloadHttpUrl(imageUrl, "image", null, null)
        return if (path != null) {
            val externalResource: ExternalResource = path.toFile().toExternalResource()
            subject.uploadImage(externalResource)
        } else {
            null
        }
    }

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS, uuid = "703065f9c4534286b21f7e8b22b1f9f6")
    @Throws(Exception::class)
    suspend fun showAudio(sender: User, messageChain: MessageChain, group: Group): Message? {
        val content = messageChain.contentToString()
        val audioUrl = checkRemoteAudio(content) ?: return null
        val path = FileUtil.downloadHttpUrl(audioUrl, "audio", null, null)
        return if (path != null) {
            val externalResource: ExternalResource = path.toFile().toExternalResource()
            group.uploadVoice(externalResource)
        } else {
            null
        }
    }

    @Command(commandType = CommandType.GROUP, value = "火星文", pattern = Pattern.PREFIX)
    @Throws(Exception::class)
    fun mars(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val content = messageChain.contentToString()
        val marsText = content.removePrefix("火星文").trim()
        return PlainText(marsUtil.getMars(marsText))
    }

    @Scheduled(cron = "00 27 19 * * ?")
    @Throws(FileNotFoundException::class)
    fun memeNum() {
        // TODO: 2021/7/8  
    }
}