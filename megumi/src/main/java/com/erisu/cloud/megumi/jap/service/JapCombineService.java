package com.erisu.cloud.megumi.jap.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.jap.mapper.JapWordsMapper;
import com.erisu.cloud.megumi.jap.pojo.JapWords;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.util.RedisKey;
import com.erisu.cloud.megumi.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
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
@Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS, uuid = "71f2fff5-a1ee-4688-8de5-ad5d36240ee1")
public class JapCombineService implements ICommandService {
    @Resource
    private JapWordsMapper japWordsMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group group = (Group) subject;
        String redisKey = String.format("%s:jap-lock+:%d", RedisKey.PLUGIN.getName(), group.getId());
        String value = redisUtil.get(redisKey);
        if ("1".equals(value)) {
            // 后续修改为从redis中获取
            String message = ((PlainText) messageChain.get(1)).getContent();
            QueryWrapper<JapWords> wrapper = new QueryWrapper<>();
            wrapper.eq("pseudonym", message);
            List<JapWords> japWords = japWordsMapper.selectList(wrapper);
            // 胜利者
            String key2 = String.format("%s:jap-winner+:%d", RedisKey.PLUGIN.getName(), group.getId());
            if (CollUtil.isNotEmpty(japWords)) {
                redisUtil.set(key2, "1");
                redisUtil.delete(redisKey);
                return new PlainText("恭喜")
                        .plus(new At((Member) sender).plus(new PlainText("答对了")));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
