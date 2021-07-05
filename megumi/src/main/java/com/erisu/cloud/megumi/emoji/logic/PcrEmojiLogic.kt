package com.erisu.cloud.megumi.emoji.logic

import com.erisu.cloud.megumi.emoji.mapper.PcrEmojiMapper
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.ImageUtil
import com.erisu.cloud.megumi.util.MessageUtil
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.Image
import org.springframework.stereotype.Component
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

    suspend fun getRandomImage(group: Group): Image? {
        val (_, _, path) = emojiMapper.selectRandom()
        val imagePath = FileUtil.downloadHttpUrl(path, "image", null, null)
        return imagePath?.let { MessageUtil.generateImage(group, imagePath.toFile(), true) }
    }
}