package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author alice
 * @Date 2020/12/25 15:54
 **/
@PatternSupport(pattern = Pattern.CONTAINS)
@Component
public class ContainsStrategy implements PatternStrategy {

    @Override
    public Boolean isMatch(MessageChain messageChain, String command,String ...alias) {
        if (messageChain.get(1) instanceof PlainText) {
            String context = ((PlainText) messageChain.get(1)).getContent();
            return StrUtil.contains(context.trim(),command);
        }
        return false;
    }
}
