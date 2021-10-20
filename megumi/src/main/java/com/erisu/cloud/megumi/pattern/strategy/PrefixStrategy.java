package com.erisu.cloud.megumi.pattern.strategy;

import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.MessageChain;
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
    public Boolean isMatch(MessageChain messageChain, String botPrefix, String command, String... alias) {
        List<String> commands = new ArrayList<>();
        commands.add(command);
        commands.addAll(Arrays.asList(alias));
        String context = messageChain.contentToString();
        Optional<String> any = commands.stream().filter(c -> context.startsWith(botPrefix + c)).findAny();
        return any.isPresent();
    }

}
