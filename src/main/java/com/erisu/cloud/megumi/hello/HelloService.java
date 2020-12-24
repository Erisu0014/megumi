package com.erisu.cloud.megumi.hello;

import com.erisu.cloud.megumi.analysis.annotation.PreAnalysis;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.command.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

/**
 * @Description hello world girl
 * @Author alice
 * @Date 2020/12/10 16:25
 **/
@Slf4j
@Component
@Command(commandType = CommandType.FRIEND, value = "在吗？", pattern = Pattern.EQUALS)
public class HelloService implements ICommandService {

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        log.info("thinking");
        return new PlainText("主様？");
    }




}
