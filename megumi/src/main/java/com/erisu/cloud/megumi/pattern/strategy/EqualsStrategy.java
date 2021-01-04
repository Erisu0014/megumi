package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

/**
 * @Description equals
 * @Author alice
 * @Date 2020/12/28 16:41
 **/
@PatternSupport(pattern = Pattern.EQUALS)
@Component
public class EqualsStrategy implements PatternStrategy {
    @Override
    public Boolean isMatch(MessageChain messageChain, String command) {
        if (messageChain.get(1) instanceof PlainText) {
            String context = ((PlainText) messageChain.get(1)).getContent();
            return StrUtil.equals(context.trim(), command);
        }
        return false;
    }
}
