package com.erisu.cloud.megumi.jap.service;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.jap.mapper.JapWordsMapper;
import com.erisu.cloud.megumi.jap.pojo.JapWords;
import com.erisu.cloud.megumi.message.MessageUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.util.RedisKey;
import com.erisu.cloud.megumi.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description 测单词
 * @Author alice
 * @Date 2020/12/29 9:52
 **/
@Slf4j
@Component
@Command(commandType = CommandType.GROUP, value = "猜单词", pattern = Pattern.EQUALS, alias = {"测单词"})
public class JapTestService implements ICommandService {
    @Resource
    private JapWordsMapper japWordsMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private MessageUtil messageUtil;

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group group = (Group) subject;
        String key = String.format("%s:jap-lock+:%d", RedisKey.PLUGIN.getName(), group.getId());
        if (redisUtil.hasKey(key)) {
            return new PlainText("上一个单词还没有猜对哦~");
        }
        List<JapWords> japWordsList = japWordsMapper.selectRandom(1);
        if (japWordsList == null || japWordsList.isEmpty()) {
            return new PlainText("唔，出问题了，联系爱丽丝姐姐看看吧");
        }
        JapWords words = japWordsList.get(0);
        // 胜利者
        String key2 = String.format("%s:jap-winner+:%d", RedisKey.PLUGIN.getName(), group.getId());
        messageUtil.sendAsyncMessageAwait(key2,group, new PlainText(String.format("很遗憾没有人猜对，%s的假名是%s", words.getWord(), words.getPseudonym())), 20);
        redisUtil.setEx(key,
                "1", 20, TimeUnit.SECONDS);
        return new PlainText(String.format("%s的假名是什么呢？20s后公布正确答案", words.getWord()));
    }

//    @Async

}
