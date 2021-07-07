package com.erisu.cloud.megumi.hello

import cn.hutool.core.util.StrUtil
import com.erisu.cloud.megumi.battle.util.MarsUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.FileUtil
import com.erisu.cloud.megumi.util.MessageUtil
import com.erisu.cloud.megumi.util.PatternUtil.checkRemoteAudio
import com.erisu.cloud.megumi.util.PatternUtil.checkRemoteImage
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
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

    @Command(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.CONTAINS, alias = ["zaima", "zai"])
    @Throws(Exception::class)
    fun hello(sender: User, messageChain: MessageChain, subject: Contact): Message {
        return PlainText("はい！私はいつも貴方の側にいるよ～")
    }

    @Command(commandType = CommandType.GROUP, value = "谁是曲奇", pattern = Pattern.EQUALS)
    @Throws(Exception::class)
    suspend fun shigetora(sender: User, messageChain: MessageChain, group: Group): Message {
        return MessageUtil.generateAudio(group, ClassPathResource("shigetora.m4a").inputStream)
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
    } //    @Command(commandType = CommandType.GROUP, value = "727", pattern = Pattern.EQUALS)
    //    public Message insaneNum727(User sender, MessageChain messageChain, Contact subject) throws Exception {
    //
    //    }
}