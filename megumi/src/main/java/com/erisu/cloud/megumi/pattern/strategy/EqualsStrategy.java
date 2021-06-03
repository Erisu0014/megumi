package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description equals
 * @Author alice
 * @Date 2020/12/28 16:41
 **/
@PatternSupport(pattern = Pattern.EQUALS)
@Component
public class EqualsStrategy implements PatternStrategy {
    @Override
    public Boolean isMatch(MessageChain messageChain, String command, String... alias) {
        List<String> commands = new ArrayList<>();
        commands.add(command);
        commands.addAll(Arrays.asList(alias));
        if (messageChain.get(1) instanceof PlainText) {
            String context = ((PlainText) messageChain.get(1)).getContent();
            Optional<String> any = commands.stream().filter(c -> StrUtil.equals(c, context.trim())).findAny();
            return any.isPresent();
        }
        return false;
    }
}
