package com.erisu.cloud.megumi.jap.service;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.pattern.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Description 测单词
 * @Author alice
 * @Date 2020/12/29 9:52
 **/
@Slf4j
@Component
@Command(commandType = CommandType.GROUP, value = "测单词", pattern = Pattern.EQUALS)
public class JapService implements ICommandService {

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        return new PlainText("不想写惹");
    }
}
