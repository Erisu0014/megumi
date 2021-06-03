package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.NumberUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.stereotype.Component;

/**
 * @Description check something
 * @Author alice
 * @Date 2021/1/6 9:07
 **/
@PatternSupport(pattern = Pattern.CHECK)
@Component
public class CheckStrategy implements PatternStrategy {
    @Override
    public Boolean isMatch(MessageChain messageChain, String command,String ...alias) {
        SingleMessage singleMessage = messageChain.get(1);
        if (singleMessage instanceof PlainText) {
            String context = ((PlainText) singleMessage).getContent();
            return NumberUtil.isNumber(context);
        }
        return false;
    }

}
