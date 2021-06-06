package com.erisu.cloud.megumi.hello;

import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.pattern.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Description hello world girl
 * @Author alice
 * @Date 2020/12/10 16:25
 **/
@Slf4j
@Component
@Model(name = "hello")
//@Command(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.CONTAINS, alias = {"zaima", "zai"})
public class HelloService {
    @Command(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.CONTAINS, alias = {"zaima", "zai"})
    public Message hello(User sender, MessageChain messageChain, Contact subject) throws Exception {
        return new PlainText("はい！私はいつも貴方の側にいるよ～");
    }

//    /**
//     * 随机复读
//     * @param sender
//     * @param messageChain
//     * @param subject
//     * @return
//     * @throws Exception
//     */
//    @Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS)
//    public Message helloKira(User sender, MessageChain messageChain, Contact subject) throws Exception {
//        // TODO: 2021/6/6 后续 直接用redis做得了
//        return new PlainText("主人你还写不写kira插件呀");
//    }


}
