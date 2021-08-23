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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.FileNotFoundException
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
    @Value("\${qq.username}")
    private var username: Long = 0

    @Resource
    private lateinit var marsUtil: MarsUtil

    @Resource
    private lateinit var redisUtil: RedisUtil

//    @Command(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.EQUALS, alias = ["zaima", "zai"])
//    @Throws(Exception::class)
//    fun hello(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        return PlainText("はい！私はいつも貴方の側にいるよ～")
//    }


//    @Command(commandType = CommandType.GROUP, value = "test1234", pattern = Pattern.EQUALS)
//    @Throws(Exception::class)
//    suspend fun test1234(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        return MessageUtil.generateAudio(subject as Group, ClassPathResource("test.mp3").inputStream)
//    }


//    @Command(commandType = CommandType.GROUP, value = "(.+)(？|\\?)", pattern = Pattern.REGEX, probaility = 0.2)
//    @Throws(Exception::class)
//    fun test(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        val forward: ForwardMessage = buildForwardMessage(subject) {
//            add(572617659, "小路", PlainText("kmr：在做了"))
////            add(sender, PlainText("你说得对"))
////            add(sender) {
////                // this: MessageChainBuilder
////                add(At(sender.id))
////                add(PlainText("msg"))
////            }
////            add(event) // 可添加 MessageEvent
//        }
//        return forward
//    }

    /**
     * 打卡下班
     */
    @Scheduled(cron = "0 30 17 * * ?")
    fun clockOut() {
        GlobalScope.future {
            val bot = Bot.getInstance(username)
            val groupId = 705366200L
            val group = bot.getGroup(groupId) as Group
            group.sendMessage(messageChainOf(PlainText("提醒下班小助手")))
        }
    }

//    @MiraiExperimentalApi
//    @Command(commandType = CommandType.GROUP, value = "test2", pattern = Pattern.EQUALS)
//    @Throws(Exception::class)
//    fun test2(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        return buildXmlMessage(1) {
//            flag = 0
//            brief = "色xcw"
//            action = "web"
//            url = "https://redive.estertion.win/sound/unit_battle_voice/111101/vo_btl_111101_ub_200.m4a"
//            item {
//                layout = 2
//                title("xcw已加入该会话")
//                picture("http://gchat.qpic.cn/gchatpic_new/1269732086/826119271-2371198257-766C3EAF0B7DDE7AE876BBF5171BA325/0")
//                summary("xcw已开始监控聊天")
//            }
//            source("不许发xcw色图")
//        }
////        return buildXmlMessage(1) {
////            flag = 0
////            brief = "群聊的聊天记录"
////            action = "web"
////            url =
////                "https://vdse.bdstatic.com//192d9a98d782d9c74c96f09db9378d93.mp4?authorization=bce-auth-v1/40f207e648424f47b2e3dfbb1014b1a5/2021-07-12T02:14:24Z/-1/host/530146520a1c89fb727fbbdb8a0e0c98ec69955459aed4b1c8e00839187536c9"
////            item {
////                layout = 6
////                title("xcw已加入该会话")
////                picture("http://gchat.qpic.cn/gchatpic_new/1269732086/826119271-2371198257-766C3EAF0B7DDE7AE876BBF5171BA325/0")
////                summary("木村唯人：日服情报偷跑了\n木村唯人：美空和兰法要实装了\n木村唯人：就是下一个fes池子\n木村唯人：[图片]")
////                summary("查看23条转发消息")
////            }
////            source("")
////        }
//
//    }

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

//    @Command(commandType = CommandType.GROUP, value = "切换图片模式", pattern = Pattern.EQUALS)
//    @Throws(Exception::class)
//    fun changeModel(sender: User, messageChain: MessageChain, group: Group): Message {
//        redisUtil.set("model", MessageModel.IMAGE.type.toString())
//        return PlainText("切换成功")
//    }

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

    @Command(commandType = CommandType.GROUP, value = "半月刊", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun halfMonth(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val halfMonth = withContext(Dispatchers.IO) { ClassPathResource("basic/半月刊.jpg").inputStream }
        return MessageUtil.generateImage(subject as Group, halfMonth)
    }

    @Command(commandType = CommandType.GROUP, value = "规划", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun bcrPlan(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val halfMonth = withContext(Dispatchers.IO) { ClassPathResource("basic/规划.jpg").inputStream }
        return MessageUtil.generateImage(subject as Group, halfMonth)
    }

    @Command(commandType = CommandType.GROUP, value = "孤儿装", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun krEquipment(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val halfMonth = withContext(Dispatchers.IO) { ClassPathResource("basic/孤儿装.jpg").inputStream }
        return MessageUtil.generateImage(subject as Group, halfMonth)
    }

    @Scheduled(cron = "00 27 19 * * ?")
    @Throws(FileNotFoundException::class)
    fun memeNum() {
        // TODO: 2021/7/8  
    }
}