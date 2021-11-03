package com.erisu.cloud.megumi.remind.service;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.remind.logic.RemindMeLogic;
import com.erisu.cloud.megumi.remind.logic.RemindTestLogic;
import com.erisu.cloud.megumi.remind.pojo.DevRemind;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Description 用以存储一些开发中任务及灵感(为什么你不用todo呢
 * @Author alice
 * @Date 2021/6/11 16:30
 **/
@Component
@Model(name = "remind")
public class RemindService {
    @Resource
    private RemindMeLogic remindMeLogic;

    @Command(value = "todo", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    public Message addRemindMe(User sender, MessageChain messageChain, Contact subject) {
        Group group = (Group) subject;
        MessageChain message = RemindTestLogic.INSTANCE.remindMe(messageChain);
        String messageStr = MessageChain.serializeToJsonString(message);
        remindMeLogic.addRemindMe(new DevRemind(null, String.valueOf(group.getId()), String.valueOf(sender.getId()), messageStr));
        return new PlainText("已添加提醒事项");
    }

    @Command(value = "/todo", commandType = CommandType.GROUP, pattern = Pattern.EQUALS)
    public Message showRemindMe(User sender, MessageChain messageChain, Contact subject) {
        Group group = (Group) subject;
        return remindMeLogic.getRemindMe(String.valueOf(group.getId()), String.valueOf(sender.getId()));
    }

    @Command(value = "~todo", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
    public Message removeRemindMe(User sender, MessageChain messageChain, Contact subject) {
        Group group = (Group) subject;
        PlainText plainText = (PlainText) messageChain.get(1);
        String id = StrUtil.removePrefix(plainText.getContent(), "~todo").trim();
        if (StrUtil.isBlank(id)) {
            return null;
        }
        return remindMeLogic.removeRemindMe(id, String.valueOf(group.getId()), String.valueOf(sender.getId()));
    }


    public Message calendarAlert(User sender, MessageChain messageChain, Contact subject) {
        // TODO: 2021/11/3
        return null;
    }

}
