package com.erisu.cloud.megumi.event.service.hello;

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
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Description 图像测试service
 * @Author alice
 * @Date 2020/12/29 9:52
 **/
@Slf4j
@Component
@Command(commandType = CommandType.GROUP, value = "摸了", pattern = Pattern.CONTAINS)
public class HelloImageService implements ICommandService {

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        File imageTest = new File("D:\\ideaProjects\\megumi\\upload\\1.jpg");
        ExternalResource externalResource = ExternalResource.create(imageTest);
        return new At(sender.getId()).plus(new PlainText("会长我这期摸了！")).plus(subject.uploadImage(externalResource));
    }
}
