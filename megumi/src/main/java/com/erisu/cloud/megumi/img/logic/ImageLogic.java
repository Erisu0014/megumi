package com.erisu.cloud.megumi.img.logic;

import cn.hutool.core.lang.UUID;
import com.erisu.cloud.megumi.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Description 图片处理逻辑
 * @Author alice
 * @Date 2021/6/17 9:54
 **/
@Slf4j
@Component
public class ImageLogic {
    @Resource
    private ImageUtil imageUtil;

    public Message generateImage(User sender, Contact subject, String text) throws Exception {
        String imgPath = imageUtil.generateImage("classpath:emoticon/xcw-1.jpg", text);
        File image = new File(imgPath);
        ExternalResource externalResource = ExternalResource.create(image);
        Image imageSource = subject.uploadImage(externalResource);
        MessageChain message = new At(sender.getId()).plus(imageSource);
        // TODO: 2021/6/17  通过消息订阅删除图片  由于资源被占用当前file无法被删除
        return message;
    }
}
