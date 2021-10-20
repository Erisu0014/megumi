package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.MessageChain;
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
    public Boolean isMatch(MessageChain messageChain, String botPrefix, String command, String... alias) {
        // TODO: 2021/10/20 alias 
        return StrUtil.contains(messageChain.contentToString().trim(), botPrefix + command);
    }
}
