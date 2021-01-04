package com.erisu.cloud.megumi.pattern.strategy;

import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.PatternSupport;
import net.mamoe.mirai.message.data.GroupImage;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.stereotype.Component;

/**
 * @Description 图片一致性比较
 * @Author alice
 * @Date 2020/12/29 15:00
 **/
@PatternSupport(pattern = Pattern.MD5)
@Component
public class ImageCheckStrategy implements PatternStrategy {
    @Override
    public Boolean isMatch(MessageChain messageChain, String command) {
        if (messageChain.get(1) instanceof GroupImage) {
            //todo just test
            return ((GroupImage) messageChain.get(1)).getImageId().equals("{384CA435-D90D-CF9C-C547-AF8F42435314}.mirai");
        }
        return false;
    }
}
