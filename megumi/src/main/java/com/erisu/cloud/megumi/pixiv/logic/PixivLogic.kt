package com.erisu.cloud.megumi.pixiv.logic

import cn.hutool.core.img.ImgUtil
import cn.hutool.core.lang.UUID
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.Font
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

/**
 *@Description pixiv logic
 *@Author alice
 *@Date 2021/10/8 14:46
 **/
@Component
class PixivLogic {
//    suspend fun getPixivCatImage(messageChain: MessageChain, subject: Contact): Message? {
//        val group = subject as Group
//        val artwork =
//            Regex("https?://www.pixiv.net/artworks/(.+)").find(messageChain.contentToString())!!.groupValues[1]
//        val artName = if (artwork.toIntOrNull() == null) {
//            val find = Regex("([0-9]+)#big_([0-9])").find(artwork) ?: return null
//            """${find.groupValues[1]}-${find.groupValues[2].toInt() + 1}"""
//        } else {
//            artwork
//        }
//        val trueFileName =
//            "${System.getProperty("user.dir")}${File.separator}cache${File.separator}${artName}.png"
//        var image: Image? = null
//        if (!File(trueFileName).exists()) {
//            // 下载图片
//            val picResponse =
//                FileUtil.downloadHttpUrl("http://www.pixiv.cat/${artName}.png", "cache", null, null) ?: return null
//            if (picResponse.code != 200) return null
//            ImgUtil.pressText(picResponse.path!!.toFile(),
//                File(trueFileName),
//                "版权所有:alice${UUID.fastUUID()}",
//                Color.WHITE,
//                Font("黑体", Font.BOLD, 1),
//                0,
//                0,
//                0.0f)
//        }
//        try {
//            image = StreamMessageUtil.generateImage(group, File(trueFileName), false)
//        } catch (e: Exception) {
//            println("图片下载失败")
//        }
//        return if (image != null) {
//            buildForwardMessage(subject) {
//                add(3347359415, "香草光钻", PlainText("http://www.pixiv.cat/${artName}.png"))
//                add(3347359415, "香草光钻", image)
//            }
//        } else {
//            PlainText("http://www.pixiv.cat/${artwork}.png")
//        }
//    }

    suspend fun getPixivCatImage(messageChain: MessageChain, subject: Contact): Message? {
        val group = subject as Group
        val artwork =
            Regex("https?://www.pixiv.net/artworks/(.+)").find(messageChain.contentToString())!!.groupValues[1]
        val trueFileName =
            "${System.getProperty("user.dir")}${File.separator}cache${File.separator}${artwork}.png"
        val images: MutableList<Image> = mutableListOf()
        val paths: MutableList<Path> = mutableListOf()
        if (!File(trueFileName).exists()) {
            // 下载图片
            val pic =
                FileUtil.downloadHttpUrl("http://www.pixiv.cat/${artwork}.png", "cache", null, null) ?: return null
            if (pic.code == 404) {
                var pixivNum: Int = 0
                val regex = Regex("<p>這個作品ID中有 ([0-9]+) 張圖片", RegexOption.DOT_MATCHES_ALL)
                pic.response?.let {
                    val find = regex.find(it)
                    if (find?.groupValues != null) {
                        pixivNum = find.groupValues[1].toInt()
                    }
                }
                for (i in 1..pixivNum) {
                    val fn = "${System.getProperty("user.dir")}${File.separator}cache${File.separator}${artwork}-$i.png"
                    if (!File(fn).exists()) {
                        val p = FileUtil.downloadHttpUrl("http://www.pixiv.cat/${artwork}-$i.png", "cache", null, null)
                            ?: return null
                        if (p.code == 200) {
                            p.path?.let { paths.add(it) }
                        }
                    } else {
                        paths.add(Paths.get(fn))
                    }

                }
            } else {
                pic.path?.let { paths.add(it) }
            }
            val nodes: MutableList<ForwardMessage.Node> = mutableListOf()
            paths.forEach {
                val image = getImage(group, it)
//                images.add(image)
                val node: ForwardMessage.Node =
                    ForwardMessage.Node(3099396879L, System.currentTimeMillis().toInt(), "诗姐姐", image)
                nodes.add(node)
            }
//            return messageChainOf(*images.toTypedArray())
            return buildForwardMessage(subject) {
                addAll(nodes)
            }
        }
        return null
    }

    suspend fun getImage(group: Group, path: Path): Image {
        ImgUtil.pressText(
            path.toFile(),
            File(path.pathString),  //  覆盖式重写
            "版权所有:alice${UUID.fastUUID()}",
            Color.WHITE,
            Font("黑体", Font.BOLD, 1),
            0,
            0,
            0.0f
        )
        return StreamMessageUtil.generateImage(group, File(path.pathString), false)
    }

}