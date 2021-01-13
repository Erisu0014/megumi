package com.erisu.cloud.megumi.jap.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
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
@Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS)
public class JapSearchService implements ICommandService {
    @Resource
    private JapWordsMapper japWordsMapper;

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        String message = ((PlainText) messageChain.get(1)).getContent();
        QueryWrapper<JapWords> wrapper = new QueryWrapper<>();
        wrapper.eq("word", message);
        List<JapWords> japWords = japWordsMapper.selectList(wrapper);
        if (japWords == null || japWords.isEmpty()) {
            return null;
        }
        JapWords words = japWords.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(words.getWord()).append("(").append(words.getPseudonym()).append(")")
                .append("\t").append(words.getChinese());
        return new PlainText(sb.toString());
    }

//    @Async

}
