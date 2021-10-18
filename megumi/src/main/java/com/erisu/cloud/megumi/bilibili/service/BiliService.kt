package com.erisu.cloud.megumi.bilibili.service

import com.erisu.cloud.megumi.bilibili.logic.BiliSearchLogic
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.RedisKey
import com.erisu.cloud.megumi.util.RedisUtil
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Description get help info
 * @Author alice
 * @Date 2021/10/11 10:41
 */
@Component
@Model(name = "bilibili", uuid = "49bd68a24eff469187d777131012404f")
class BiliService {
    @Resource
    private lateinit var biliSearchLogic: BiliSearchLogic

    @Resource
    private lateinit var redisUtil: RedisUtil

    @Command(commandType = CommandType.GROUP,
        value = "查成分 ",
        pattern = Pattern.PREFIX,
        uuid = "ed386314c7124c619a0025934a0b7a1d")
    @Throws(Exception::class)
    fun searchVtb(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val user = messageChain.contentToString().removePrefix("查成分 ").trim()
        if (user.length > 40) return null
        val pair = biliSearchLogic.searchUser(user)
        if (pair != null) {
            val follow = biliSearchLogic.searchFollow(pair.second)
            if (!follow.isNullOrEmpty()) {
                val vtbs = biliSearchLogic.checkDD(follow)
                return if (vtbs != null && vtbs.size != 0) {
                    messageChainOf(PlainText("${pair.first}关注的皮套人有${vtbs.size}位，他们是：\n${
                        vtbs.joinToString(separator = ",")
                    }"))
                } else {
                    messageChainOf(PlainText("${pair.first}没有关注皮套人，干的漂亮！"))
                }
            }
        } else {
            return PlainText("这位更是歌\uD83C\uDFA4")
        }
        return null
    }

    @Command(commandType = CommandType.GROUP,
        value = "绑定(b|B)站 ([0-9]){3,8}",
        pattern = Pattern.REGEX)
    fun bindUid(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val mid = messageChain.contentToString().substring(6)
        redisUtil.set(RedisKey.BILIBILI.key + ":" + sender.id,mid)
        return PlainText("绑定成功喵")
    }

    fun unbindUid(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        return PlainText("解绑成功喵")
    }


}