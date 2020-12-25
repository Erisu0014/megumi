package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.Message;
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
    public Boolean isMatch(Message message,String command) {
        if (message instanceof PlainText) {
            String context = ((PlainText) message).getContent();
            return StrUtil.contains(context.trim(),command);
        }
        return false;
    }
}
