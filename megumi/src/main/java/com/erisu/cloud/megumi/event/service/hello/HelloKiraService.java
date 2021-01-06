package com.erisu.cloud.megumi.event.service.hello;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.pattern.Pattern;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

/**
 * @Description kira~⭐
 * @Author alice
 * @Date 2020/12/29 10:08
 **/
@Command(commandType = CommandType.GROUP,pattern = Pattern.CONTAINS,value = "kira")
@Component
public class HelloKiraService implements ICommandService {
    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        return new PlainText("主人你还写不写kira插件呀");
    }
}
