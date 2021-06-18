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
@PatternSupport(pattern = Pattern.REGEX)
@Component
public class RegexStrategy implements PatternStrategy {
    /**
     * 正则匹配不需要alias(暂时)
     *
     * @param messageChain
     * @param command
     * @param alias
     * @return
     */
    @Override
    public Boolean isMatch(MessageChain messageChain, String command, String... alias) {
        SingleMessage singleMessage = messageChain.get(1);
        return java.util.regex.Pattern.matches(command, singleMessage.toString().trim());
    }

}
