package com.erisu.cloud.megumi.event.service.hello;

import cn.hutool.core.io.FileUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.pattern.Pattern;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * @Description kira~⭐
 * @Author alice
 * @Date 2020/12/29 10:08
 **/
@Command(commandType = CommandType.GROUP, pattern = Pattern.EQUALS, value = "ub")
@Component
public class HelloAudioService implements ICommandService {
    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group group = (Group) subject;
        InputStream inputStream = FileUtil.getInputStream("D:\\ideaProjects\\megumiBot\\megumi\\src\\main\\resources\\vo_btl_104301_ub_200.m4a");
        ExternalResource externalResource = ExternalResource.create(inputStream);
        return group.uploadVoice(externalResource);
    }
}
