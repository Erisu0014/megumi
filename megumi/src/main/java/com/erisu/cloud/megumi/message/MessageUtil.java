package com.erisu.cloud.megumi.message;

import com.erisu.cloud.megumi.util.RedisUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description send message
 * @Author alice
 * @Date 2021/1/13 16:54
 **/
@Component
public class MessageUtil {
    @Value("${qq.username}")
    private long username;
    @Resource
    private RedisUtil redisUtil;

    @Async
    public void sendAsyncMessage(Group group, Message message, Integer time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
        Bot.getInstance(username).getGroup(group.getId()).sendMessage(message);
    }

    /**
     * @param redisKey 基于redis决定任务是否发送（一般场景） 后续考虑如何更轻便 --todo 建议想好了重写
     * @param group
     * @param message
     * @param time     睡眠秒数
     * @throws InterruptedException
     */
    @Async
    public void sendAsyncMessageAwait(String redisKey, Group group, Message message, Integer time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
        if (!redisUtil.hasKey(redisKey)) {
            Bot.getInstance(username).getGroup(group.getId()).sendMessage(message);
        } else {
            redisUtil.delete(redisKey);
        }
    }
}
