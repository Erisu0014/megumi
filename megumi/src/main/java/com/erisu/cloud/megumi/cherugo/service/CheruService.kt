package com.erisu.cloud.megumi.cherugo.service

import cn.hutool.core.util.StrUtil
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.pattern.Pattern
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.CheRuEncoder.cheru2word
import com.erisu.cloud.megumi.util.CheRuEncoder.word2cheru
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component

/**
 *@Description 切噜~
 *@Author alice
 *@Date 2022/5/9 15:22
 **/
@Component
@Model(name = "cheru", help = """
[切噜一下...]：将后面的文字转化为切噜语
[切噜～♪]：将切噜语转为原句
""")
class CheruService {
    @Command(value = "切噜一下", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    fun word2cheru(sender: User?, messageChain: MessageChain, subject: Contact?): Message? {
        val result = word2cheru(StrUtil.removePrefix(messageChain.content, "切噜一下"))
        return PlainText("切噜～♪$result")
    }

    @Command(value = "切噜～♪", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    fun cheru2word(sender: User, messageChain: MessageChain, subject: Contact?): Message? {
        val result = cheru2word(StrUtil.removePrefix(messageChain.content, "切噜～♪"))
        return At(sender.id).plus(PlainText(String.format("的切噜语是：\n切噜～♪%s", result)))
    }
}