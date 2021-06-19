package com.erisu.cloud.megumi.img.service;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.img.logic.ImageLogic;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 图片相关
 * @Author alice
 * @Date 2021/6/17 9:47
 **/
@Model(name = "img")
@Component
public class ImgService {
    @Resource
    private ImageLogic imageLogic;

    @Command(commandType = CommandType.GROUP, value = ".jpg", pattern = Pattern.SUFFIX, uuid = "ff33a026c184430fb88fb1e49ee4bf25")
    public Message generateImg(User sender, MessageChain messageChain, Contact subject) throws Exception {
        PlainText plainText = (PlainText) messageChain.get(1);
        String text = StrUtil.removeSuffix(plainText.getContent(), ".jpg");
        return imageLogic.generateImage(sender, subject, text);
    }
}
