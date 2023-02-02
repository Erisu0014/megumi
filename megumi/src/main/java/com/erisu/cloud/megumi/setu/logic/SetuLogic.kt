package com.erisu.cloud.megumi.setu.logic

import cn.hutool.core.img.ImgUtil
import cn.hutool.core.lang.UUID
import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.setu.pojo.SetuRequest
import com.erisu.cloud.megumi.setu.pojo.SetuResponse
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.Font
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 *@Description setu logic
 *@Author alice
 *@Date 2021/7/7 8:46
 **/
@MiraiExperimentalApi
@Component
class SetuLogic {
    @Value("\${qq.username}")
    private val username: Long = 0

    suspend fun getRollSetu(tag: String, num: Int, isR18: Int, group: Group): Message? {
        val orTag = tag.split("|")
        val setuRequest: SetuRequest = if (tag.trim() == "") {
            SetuRequest(
                dateAfter = null,
                dateBefore = null,
                uid = null,
                tag = null,
                keyword = null,
                num = num,
                r18 = isR18
            )
        } else {
            SetuRequest(
                dateAfter = null,
                dateBefore = null,
                uid = null,
                tag = arrayOf(orTag),
                keyword = null,
                num = num, r18 = isR18
            )
        }
//        group.sendMessage(PlainText("setu正在下载中，请稍等~"))
        val responseJson =
            HttpUtil.post("https://api.lolicon.app/setu/v2", JSON.toJSONString(setuRequest), 2000)
        val nodes: MutableList<ForwardMessage.Node> = mutableListOf()
        val setuResponse = JSONObject.parseObject(responseJson, SetuResponse::class.java)
        if (setuResponse.error == "" && setuResponse.data.isNotEmpty()) {
//            val imageList: MutableList<Image> = mutableListOf()
            if (isR18 == 0) {
                setuResponse.data.forEach {
                    val name = UUID.fastUUID().toString()
                    val response =
                        FileUtil.downloadHttpUrl(it.urls.original!!, "${FileUtil.localCachePath}${File.separator}", null, name, true)
                            ?: return null
//                    if (path != null) imageList.add(StreamMessageUtil.generateImage(group, path.toFile(), true))
                    if (response.code == 200) {
                        val text = "pid：${it.pid}\n标题：${it.title}\n作者：${it.author}\n原地址：${it.urls.original}"
                        val image = getImage(group, response.path!!, "${FileUtil.localCachePath}${File.separator}${name}", true)
                        val node: ForwardMessage.Node =
                            ForwardMessage.Node(
                                2854196306, System.currentTimeMillis().toInt(),
                                "色图bot", messageChainOf(PlainText(text), image)
                            )
                        nodes.add(node)
                    }
                }
                return if(!nodes.isNullOrEmpty()){
                    buildForwardMessage(group) {
                        addAll(nodes)
                    }
                }else{
                    PlainText("色图下载失败惹>.<")
                }
//                messageChainOf(*imageList.toTypedArray())
            } else {
                setuResponse.data.forEach { group.sendMessage(it.urls.original.toString()) }
//                PlainText(setuResponse.data[0].urls.original.toString())
            }
        } else if (setuResponse.data.isEmpty()) {
            print(setuResponse.error)
            return PlainText("那是什么色图？搜不到喵")
        } else {
            return PlainText("色图下载失败惹>.<")
        }
        return null
    }

    fun forwardSetuMessage(text: PlainText, image: Image, contact: Contact): Message {
        return buildForwardMessage(contact) {
            add(2854196306, "色图bot", messageChainOf(text, image))
        }
    }


    suspend fun getImage(group: Group, path: Path, name: String?, isDelete: Boolean): Image {
        val newName = name ?: path.toString()
        ImgUtil.pressText(
            path.toFile(),
            File(newName),  //  覆盖式重写
            "版权所有:alice${UUID.fastUUID()}",
            Color.WHITE,
            Font("黑体", Font.BOLD, 1),
            0,
            0,
            0.0f
        )
        return StreamMessageUtil.generateImage(group, File(newName), isDelete)
    }

    suspend fun getLocalSetu(group: Group, path: String, num: Int): Message {
        val nodes: MutableList<ForwardMessage.Node> = mutableListOf()
        for (i in 0 until num) {
            val randomFile = FileUtil.getRandomFile(path, null)
            val file = Paths.get(randomFile)
            val image = getImage(group, file, "${FileUtil.localCachePath}${File.separator}${UUID.fastUUID()}.png", true)
            val node: ForwardMessage.Node =
                ForwardMessage.Node(3347359415L, System.currentTimeMillis().toInt(), "jackeylove", image)
            nodes.add(node)
        }
        return buildForwardMessage(group) {
            addAll(nodes)
        }

    }

}