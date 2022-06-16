package com.erisu.cloud.megumi.emoji.logic

import com.erisu.cloud.megumi.emoji.mapper.PcrEmojiMapper
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.StreamMessageUtil
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Image
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.Resource

/**
 *@Description pcr表情逻辑处理
 *@Author alice
 *@Date 2021/6/25 16:39
 **/
@Slf4j
@Component
class PcrEmojiLogic {
    @Resource
    private lateinit var emojiMapper: PcrEmojiMapper

    suspend fun getRandomImage(group: Group): Image {
//        val (_, _, path) = emojiMapper.selectRandom()
        val filePath = FileUtil.getRandomFile("emoji", null)
//        val imageResponse = FileUtil.downloadHttpUrl(path, "image", null, null) ?: return null
//        if (imageResponse.code != 200) return null
        return StreamMessageUtil.generateImage(group, File(filePath), false)
    }
}