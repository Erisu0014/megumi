package com.erisu.cloud.megumi.cherugo.service;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.util.CheRuEncoder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

/**
 * @Description 切噜~
 * @Author alice
 * @Date 2021/6/15 17:14
 **/
@Component
@Model(name = "cheru")
public class CheruService {
    @Command(value = "切噜一下", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    public Message word2cheru(User sender, MessageChain messageChain, Contact subject) {
        String content = ((PlainText) messageChain.get(1)).getContent();
        String result = CheRuEncoder.INSTANCE.word2cheru(StrUtil.removePrefix(content, "切噜一下"));
        return new PlainText(String.format("切噜～♪%s", result));
    }

    @Command(value = "切噜～♪", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    public Message cheru2word(User sender, MessageChain messageChain, Contact subject) {
        String content = ((PlainText) messageChain.get(1)).getContent();
        String result = CheRuEncoder.INSTANCE.cheru2word(StrUtil.removePrefix(content, "切噜～♪"));
        return new At(sender.getId()).plus(new PlainText(String.format("的切噜语是：\n切噜～♪%s", result)));
    }
}
