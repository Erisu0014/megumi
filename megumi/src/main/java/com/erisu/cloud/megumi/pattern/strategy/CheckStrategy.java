package com.erisu.cloud.megumi.pattern.strategy;

import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.stereotype.Component;

/**
 * @Description 检查是否为文本信息
 * @Author alice
 * @Date 2021/1/6 9:07
 **/
@PatternSupport(pattern = Pattern.CHECK)
@Component
public class CheckStrategy implements PatternStrategy {
    @Override
    public Boolean isMatch(MessageChain messageChain, String command, String... alias) {
        SingleMessage singleMessage = messageChain.get(1);
        return singleMessage instanceof PlainText;
    }

}
