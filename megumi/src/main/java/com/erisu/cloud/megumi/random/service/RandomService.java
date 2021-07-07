package com.erisu.cloud.megumi.random.service;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.random.logic.RandomLogic;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.stereotype.Component;

/**
 * @Description 随机相关
 * @Author alice
 * @Date 2021/6/15 10:08
 **/
@Component
@Model(name = "random")
public class RandomService {
    /**
     * /roll $num?  $num? $question?
     *
     * @param sender
     * @param messageChain
     * @param subject
     * @return
     */
    @Command(value = "/roll", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    public Message roll(User sender, MessageChain messageChain, Contact subject) {
        return RandomLogic.INSTANCE.roll(messageChain);
    }
}
