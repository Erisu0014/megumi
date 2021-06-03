package com.erisu.cloud.megumi.event.service.hello;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.CommandV2;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.event.service.plugin.pojo.Model;
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
    @CommandV2(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.CONTAINS, alias = {"zaima", "zai"})
    public Message hello(User sender, MessageChain messageChain, Contact subject) throws Exception {
        return new PlainText("はい！私はいつも貴方の側にいるよ～");
    }

    @CommandV2(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS, value = "kira")
    public Message helloKira(User sender, MessageChain messageChain, Contact subject) throws Exception {
        return new PlainText("主人你还写不写kira插件呀");
    }

    @CommandV2(commandType = CommandType.GROUP, value = "摸了", pattern = Pattern.CONTAINS)
    public Message helloMo(User sender, MessageChain messageChain, Contact subject) throws Exception {
        File imageTest = new File("D:\\ideaProjects\\megumi\\upload\\1.jpg");
        ExternalResource externalResource = ExternalResource.create(imageTest);
        return new At(sender.getId()).plus(new PlainText("会长我这期摸了！")).plus(subject.uploadImage(externalResource));
    }

}
