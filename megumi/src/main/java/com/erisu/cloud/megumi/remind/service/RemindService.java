package com.erisu.cloud.megumi.remind.service;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.remind.logic.RemindTestLogic;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author alice
 * @Date 2021/6/11 16:30
 **/
@Component
@Model(name = "remind")
public class RemindService {
    @Command(value = "!提醒", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    public Message remindMe(User sender, MessageChain messageChain, Contact subject) {
        Group group = (Group) subject;
        return RemindTestLogic.INSTANCE.remindMe(messageChain);
    }
}
