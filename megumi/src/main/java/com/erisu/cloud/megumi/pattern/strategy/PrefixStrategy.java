package com.erisu.cloud.megumi.pattern.strategy;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description check something
 * @Author alice
 * @Date 2021/1/6 9:07
 **/
@PatternSupport(pattern = Pattern.PREFIX)
@Component
public class PrefixStrategy implements PatternStrategy {
    @Override
    public Boolean isMatch(MessageChain messageChain, String command, String... alias) {
        SingleMessage singleMessage = messageChain.get(1);
        List<String> commands = new ArrayList<>();
        commands.add(command);
        commands.addAll(Arrays.asList(alias));
        if (singleMessage instanceof PlainText) {
            String context = ((PlainText) singleMessage).getContent();
            Optional<String> any = commands.stream().filter(context::startsWith).findAny();
            return any.isPresent();
        }
        return false;
    }

}
