package com.erisu.cloud.megumi.osu.logic

import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.osu.pojo.BeatMapInfo
import com.erisu.cloud.megumi.osu.pojo.BgUtil
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import okhttp3.*
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import kotlin.io.path.name
import kotlin.io.path.pathString

/**
 *@Description TODO
 *@Author alice
 *@Date 2023/2/16 9:22
 **/
@Component
class OsuLogic {
    companion object {
        var osuBgPath = "${FileUtil.localStaticPath}${File.separator}osu${File.separator}bg"
        var osuTempPath = "${FileUtil.localStaticPath}${File.separator}osu${File.separator}temp"

        const val baseUrl = "https://osu.ppy.sh/api/v2"
        var token = {
            val path = "${FileUtil.localStaticPath}${File.separator}osu${File.separator}token.txt"
            File(path).readLines().joinToString(separator = "")
        }
    }

    @Resource
    lateinit var redisUtil: RedisUtil
    fun getMapInfo(beatmapId: String): BeatMapInfo? {
        val url = "${baseUrl}/beatmaps/${beatmapId}"
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        var beatMapInfo: BeatMapInfo? = null
        val response = client.newCall(
            Request.Builder().header("Authorization", "Bearer ${token()}").url(url).build()
        ).execute()
        if (!response.isSuccessful) {
            throw Exception("response返回异常，错误码:${response.code}")
        }
        beatMapInfo = JSONObject.parseObject(response.body!!.string(), BeatMapInfo::class.java)
        return beatMapInfo
    }

    suspend fun rollOsuBg(sender: User, group: Group): Message? {
        val inGame = redisUtil.hGet(RedisKey.OSU_BG.key, group.id.toString()) != null
        if (inGame) {
            return PlainText("猜bg正在进行中>.<")
        }
        val bgPair = splitBg()
        redisUtil.hPut(RedisKey.OSU_BG.key, group.id.toString(), bgPair.first)
        val preMessage = messageChainOf(
            PlainText("猜猜这个图片是哪张map的一部分?(20s后公布答案)"),
            StreamMessageUtil.generateImage(group, File(bgPair.second), true)
        )
        group.sendMessage(preMessage)
        CoroutineScope(Dispatchers.IO).launch {
            delay(20000L)
            val winner = redisUtil.hGetAll("${RedisKey.OSU_BG_WINNER.key}:${group.id}")
            val bgPath = "${osuBgPath}${File.separator}${bgPair.first}.png"
            val image = StreamMessageUtil.generateImage(group, File(bgPath), false)
            if (winner != null) {
                group.sendMessage(
                    messageChainOf(
                        PlainText(winner.keys.joinToString { "猜对啦~\n" }),
                        PlainText("正确答案是：${BgUtil.bgJson.getJSONArray(bgPair.first)[0]}"),
                        image
                    )
                )
            } else {
                group.sendMessage(
                    messageChainOf(
                        PlainText("很遗憾没有人答对"),
                        PlainText("正确答案是：${BgUtil.bgJson.getJSONArray(bgPair.first)[0]}"),
                        image
                    )
                )
            }
        }
        return null
    }

    suspend fun bgNameCheck(sender: User, group: Group, message: Message): Message? {
        val bgId = redisUtil.hGet(RedisKey.OSU_BG.key, group.id.toString())
        if (bgId != null) {
            if (BgUtil.nameCheck(message.contentToString().trim(), bgId)) {
                redisUtil.hPut("${RedisKey.OSU_BG_WINNER.key}:${group.id}", sender.nick, "1")
            }
        }
        return null
    }

    fun splitBg(): Pair<String, String> {
        val bgPaths = FileUtil.getRandomFile(osuBgPath, null)
        val bgFile = Paths.get(bgPaths)
        //创建一个Random对象
        val random = Random()
        //生成随机的x坐标和y坐标，范围在0到250之间
        val x = random.nextInt(301)
        val y = random.nextInt(301)
        val path = "${osuTempPath}${File.separator}${UUID.randomUUID()}.png"
        //创建一个输出文件对象，假设输出目录为/data/data/com.example
        //调用Thumbnails类中的of方法和toFile方法，进行图片裁剪和输出
        Thumbnails.of(bgFile.pathString)
            .sourceRegion(Positions.TOP_LEFT, x, y) //指定裁剪区域
            .size(50, 50) //指定输出大小
            .toFile(path) //指定输出路径
        val bgId = bgFile.name.split(".")[0]
        return Pair(bgId, path)
    }
}