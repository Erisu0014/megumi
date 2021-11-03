package com.erisu.cloud.megumi.hello

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.battle.util.MarsUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.tuling.logic.TulingLogic
import com.erisu.cloud.megumi.util.BotPrefix
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.PatternUtil.checkRemoteAudio
import com.erisu.cloud.megumi.util.PatternUtil.checkRemoteImage
import com.erisu.cloud.megumi.util.StreamMessageUtil
import com.erisu.cloud.megumi.util.VoiceUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiInternalApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileNotFoundException
import javax.annotation.Resource
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


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
    private lateinit var tulingLogic: TulingLogic

    @Command(commandType = CommandType.GROUP, value = "死了吗", pattern = Pattern.EQUALS, prefix = BotPrefix.alice)
    @Throws(Exception::class)
    fun hello(sender: User, messageChain: MessageChain, subject: Contact): Message {
        return PlainText("喵？")
    }


//    @Command(commandType = CommandType.GROUP, value = "test1234", pattern = Pattern.EQUALS)
//    @Throws(Exception::class)
//    suspend fun test1234(sender: User, messageChain: MessageChain, subject: Contact): Message {
//        return StreamMessageUtil.generateAudio(subject as Group, ClassPathResource("test.mp3").inputStream)
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
//    @Async
    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(cron = "0 30 17 * * ?")
     fun clockOut() {
        GlobalScope.future {
            val bot = Bot.getInstance(username)
            val groupId = 705366200L
            val group = bot.getGroup(groupId) as Group
            val image = StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/xiaban.gif").inputStream)
            group.sendMessage(messageChainOf(PlainText("下班啦~下班啦~"), image))
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
        val path = "${FileUtil.localStaticPath}${File.separator}osu${File.separator}shigetora.m4a"
        val silkPath = VoiceUtil.convertToSilk(path)
        val silkFile = File(silkPath)
        return StreamMessageUtil.generateAudio(group, silkFile, false)
    }


    @Command(commandType = CommandType.GROUP, value = "alice起床", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun okitte(sender: User, messageChain: MessageChain, group: Group): Message {
        val path = "${FileUtil.localStaticPath}${File.separator}osu${File.separator}alice.mp3"
        val silkPath = VoiceUtil.convertToSilk(path)
        val silkFile = File(silkPath)
        return StreamMessageUtil.generateAudio(group, silkFile, false)
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
        val pathResponse = FileUtil.downloadHttpUrl(imageUrl, "image", null, null) ?: return null
        return if (pathResponse.code != 200) {
            val externalResource: ExternalResource = pathResponse.path!!.toFile().toExternalResource()
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
        val pathResponse = FileUtil.downloadHttpUrl(audioUrl, "audio", null, null) ?: return null
        return if (pathResponse.code != 200) {
            val externalResource: ExternalResource = pathResponse.path!!.toFile().toExternalResource()
            group.uploadAudio(externalResource)
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
        val file =
            File("${System.getProperty("user.dir")}${File.separator}static${File.separator}basic${File.separator}半月刊.png")
        return StreamMessageUtil.generateImage(subject as Group, file, false)
    }

    @OptIn(MiraiInternalApi::class)
    @Command(commandType = CommandType.GROUP,
        value = "更新半月刊",
        pattern = Pattern.PREFIX,
        uuid = "2f37e8fd963c410e88ecaeaf5fe8086a")
    @Throws(Exception::class)
    suspend fun updateHalfMonth(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        if (sender.id == 1269732086L) {
            if (messageChain[2] is GroupImage) {
                val image = messageChain[2] as GroupImage
                val url = image.queryUrl()
                val path =
                    FileUtil.downloadHttpUrl(url, "${FileUtil.localStaticPath}${File.separator}basic", "png", "半月刊")
                if (path != null) {
                    return PlainText("更新半月刊成功喵")
                }
            }
        }
        return PlainText("更新半月刊失败喵")
    }

    @Command(commandType = CommandType.GROUP, value = "规划", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun bcrPlan(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val file =
            File("${System.getProperty("user.dir")}${File.separator}static${File.separator}basic${File.separator}规划.jpg")
        return StreamMessageUtil.generateImage(subject as Group, file, false)
    }

    @Command(commandType = CommandType.GROUP, value = "半周年", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun bcrHalfAnniversary(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val file =
            File("${System.getProperty("user.dir")}${File.separator}static${File.separator}basic${File.separator}半周年.jpg")
        return StreamMessageUtil.generateImage(subject as Group, file, false)
    }

    @Command(commandType = CommandType.GROUP,
        value = "孤儿装",
        pattern = Pattern.EQUALS,
        uuid = "530108b76cf440ab8551d17ea2cb9e12")
    @Throws(Exception::class)
    suspend fun krEquipment(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val file =
            File("${System.getProperty("user.dir")}${File.separator}static${File.separator}basic${File.separator}孤儿装.jpg")
        return StreamMessageUtil.generateImage(subject as Group, file, false)
    }


    @Command(commandType = CommandType.GROUP,
        value = "赛程",
        pattern = Pattern.EQUALS,
        uuid = "95825f0b39c240ce8e6f11dcb0bc86d6")
    @Throws(Exception::class)
    suspend fun s11lol(sender: User, messageChain: MessageChain, subject: Contact?): Message {
        val file =
            File("${System.getProperty("user.dir")}${File.separator}static${File.separator}lol${File.separator}s11赛程.jpg")
        return StreamMessageUtil.generateImage(subject as Group, file, false)
    }



    @Command(commandType = CommandType.GROUP, value = "迫害", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun memento(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val group = subject as Group
        if (group.id != 705366200L) {
            return null
        }
        val randomFile = FileUtil.getRandomFile("${FileUtil.localStaticPath}${File.separator}memento", "png")
        return StreamMessageUtil.generateImage(group, File(randomFile), false)
    }

    @Command(commandType = CommandType.GROUP, value = "憨批诗酱", pattern = Pattern.PREFIX)
    suspend fun hanpi(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val group = subject as Group
        if (group.id != 705366200L) {
            return null
        }
        val file = File("${FileUtil.localStaticPath}${File.separator}memento${File.separator}憨批诗酱.png")
        return StreamMessageUtil.generateImage(group, file, false)
    }

    @Command(commandType = CommandType.GROUP, value = "谁敢向我挑衅", pattern = Pattern.PREFIX)
    suspend fun pogDamage(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val group = subject as Group
        if (group.id != 705366200L) {
            return null
        }
        val file = File("${FileUtil.localStaticPath}${File.separator}memento${File.separator}pogDamage.png")
        val image = StreamMessageUtil.generateImage(group, file, false)
        return messageChainOf(PlainText("谁敢向我挑衅！我将终结他的性命！无限火力诗酱Utaha百分百负胜率猴子符文已部署!"), image)
    }

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CHECK_AT, uuid = "bbd87b41253a4339a18f8013fa7a6700")
    @Throws(Exception::class)
    suspend fun eroiOnlineAnswering(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val splitWords = messageChain.contentToString().split(" ", limit = 2)
        if (splitWords[0] == "@3347359415") {
            val words = splitWords[1]
            return tulingLogic.eroOnlineAnswering(words)
        }
        return null
    }

//    @Async
    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(cron = "00 00 23 * * ?")
    @Throws(FileNotFoundException::class)
     fun alertNothing() {
        GlobalScope.future {
            val bot = Bot.getInstance(username)
            val groupId = 705366200L
            val group = bot.getGroup(groupId) as Group
            group.sendMessage(messageChainOf(PlainText("诗酱别冲了~")))

        }
    }


//    @Async
    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(cron = "00 30 08 * * ?")
    @Throws(FileNotFoundException::class)
     fun goSleep() {
        GlobalScope.future {
            val bot = Bot.getInstance(username)
            val groupId = 705366200L
            val group = bot.getGroup(groupId) as Group
            group.sendMessage(messageChainOf(PlainText("alice快睡吧~")))

        }
    }

    @Command(commandType = CommandType.GROUP,
        pattern = Pattern.REGEX,
        value = "补时([0-9]+)([秒s])(.*)",
        uuid = "6b7f91bdc34b4aa3a09527ee976ca622")
    fun timeChecker(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val values = Regex("补时([0-9]+)([秒s])(.*)",
            RegexOption.DOT_MATCHES_ALL).find(messageChain.contentToString())!!.groupValues
        val timeLine = values[3]
        val sub = 90 - values[1].toInt()
        if (sub < 0 || sub > 90) {
            return null
        }
        val newTimeLine = timeLine.replace(Regex("[0-9]{1,2}:[0-9]{2}")) {
            val split = it.groupValues[0].split(":")
            val now = split[0].toInt() * 60 + split[1].toInt() - sub
            if (now < 0) "??:??"
            else {
                var t1 = ""
                var t2 = ""
                if ((now % 60).toString().length == 1) t2 = "0"
                t2 += (now % 60).toString()
                if ((now / 60).toString().length == 1) t1 = "0"
                t1 += (now / 60).toString()
                "$t1:$t2"
            }

        }
        return messageChainOf(At(sender.id), PlainText("\n" + newTimeLine))
    }

    @Command(commandType = CommandType.GROUP,
        pattern = Pattern.REGEX,
        value = "用时([0-9]+)([秒s])([0-9]+) ([0-9]+)",
        uuid = "0c698a4360014d45865640ec6f2a6a98")
    fun rewardTime(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val values = Regex("用时([0-9]+) ([0-9]+)",
            RegexOption.DOT_MATCHES_ALL).find(messageChain.contentToString())!!.groupValues
        val damage = values[1].toInt()
        val hp = values[3].toInt()
        if (damage < hp) {
            return PlainText("这能打死boss吗？你再想想")
        }
        // TODO: 2021/9/27 这没啥意义啊感觉
        return null
    }


    @Command(commandType = CommandType.GROUP,
        pattern = Pattern.REGEX,
        value = "(.*)\"desc\":\"(.*?)\"(.*)\"preview\":\"(.*?)\"(.*)\"qqdocurl\":\"(.*?)\"(.*)",
        uuid = "1426fb0448994902b5e002649ceb619e")
    suspend fun pulipuli(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val finder =
            Regex("(.*)\"desc\":\"(.*?)\"(.*)\"preview\":\"(.*?)\"(.*)\"qqdocurl\":\"(.*?)\"(.*)").find(messageChain.contentToString())
                ?: return null
        val desc = finder.groupValues[2]
        val preview = finder.groupValues[4]
        val url = finder.groupValues[6]
        val fileResponse = FileUtil.downloadHttpUrl(preview, "cache", null, null) ?: return null
        return if (fileResponse.code == 200) {
            val imageFile = fileResponse.path!!.toFile()
            val img = StreamMessageUtil.generateImage(subject as Group, imageFile, false)
            messageChainOf(PlainText("bilibili:$desc"), img, PlainText(url))
        } else {
            null
        }
    }

//    @Async
    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(cron = "00 00 09 * * ?")
    @Throws(FileNotFoundException::class)
     fun dailyNews() {
        GlobalScope.future {
            val bot = Bot.getInstance(username)
            val groupId = 705366200L
            val group = bot.getGroup(groupId) as Group
            getDailyNews(group)?.let { group.sendMessage(it) }
        }
    }

    @Command(commandType = CommandType.GROUP,
        pattern = Pattern.EQUALS,
        value = "今日早报")
    suspend fun dailyNews(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val group = subject as Group
        return getDailyNews(group)
    }

    private suspend fun getDailyNews(group: Group): Message? {
        val res = HttpUtil.get("http://dwz.2xb.cn/zaob", 2000)
        if (res != null) {
            val json = JSONObject.parseObject(res)
            if (json["code"] == 200) {
                val url = json["imageUrl"].toString()
                val pathRes = FileUtil.downloadHttpUrl(url, "cache", null, null)
                if (pathRes != null && pathRes.code == 200) {
                    return StreamMessageUtil.generateImage(group, pathRes.path!!.toFile(), false)
                }
            }
        }
        return null
    }


    @Command(commandType = CommandType.GROUP, pattern = Pattern.PREFIX, value = "", probaility = 0.5)
    fun nbnhhsh(sender: User, messageChain: MessageChain, subject: Contact): Message? {
        val regex = Regex("([a-z]+)", RegexOption.IGNORE_CASE)
        if (!regex.matches(messageChain.contentToString())) return null
        val finder =
            regex.find(messageChain.contentToString()) ?: return null
        var text = ""
        text = finder.groupValues[1]
        // 查询不触发关键词
        val keywords = File("${FileUtil.localStaticPath}${File.separator}basic${File.separator}nbnhhsh.txt").readLines()
        if (keywords.contains(text)) return null
        val requestMap = mapOf("text" to text)
        val res = HttpUtil.post("https://lab.magiconch.com/api/nbnhhsh/guess", requestMap, 3000)
        val resPo = JSONObject.parseArray(res)
        val ja = resPo.getJSONObject(0).getJSONArray("trans")
        if (ja.isNullOrEmpty()) return PlainText("能不能好好说话\uD83D\uDE05")
        val result = JSONObject.parseArray(JSON.toJSONString(ja), String::class.java)
        return PlainText("您可能在说：${result.joinToString(separator = ",")}")
    }

    @OptIn(ExperimentalTime::class)
    @Command(commandType = CommandType.GROUP, pattern = Pattern.EQUALS, value = "水嘉然")
    suspend fun sbjiaxintang(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val bot = Bot.getInstance(username)
        val group = bot.getGroup(subject.id) as Group
        try {
            if (sender is NormalMember) {
                sender.mute(Duration.Companion.minutes(10))
            }
        } catch (e: Exception) {
            group.sendMessage("权限高了不起啊！我直接给alice告状\uD83D\uDE05")
        }

        return PlainText("嘉然小矮子是不是你爹啊天天碰瓷pcr，赶紧爬\uD83D\uDE01")
    }

    @Command(commandType = CommandType.GROUP, value = "挂树", pattern = Pattern.EQUALS, probaility = 0.3)
    @Throws(Exception::class)
    suspend fun guashu(sender: User, messageChain: MessageChain, subject: Contact): Message {
        val group = subject as Group
        return StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/guashu.jpg").inputStream)
    }


    @Command(commandType = CommandType.GROUP, value = "test1", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    fun test1234(sender: User, messageChain: MessageChain, subject: Contact): Message? {
//        clockOut()
        return null
    }


}