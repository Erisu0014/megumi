package com.erisu.cloud.megumi.img.logic;

import com.erisu.cloud.megumi.util.ImageUtil;
import com.erisu.cloud.megumi.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.CompletableFuture;

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
        CompletableFuture<Image> future = MessageUtil.INSTANCE.generateImageAsync((Group) subject, image, true);
        return future.get();
    }
}
