package com.erisu.cloud.megumi.homo;

import cn.hutool.core.util.NumberUtil;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.pattern.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 这么臭有什么写的必要吗
 * @Author alice
 * @Date 2021/1/6 9:02
 **/
@Slf4j
@Component
@Model(name = "homo")
public class HomoService {
    @Resource
    private HomoLogic homoLogic;

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CHECK, probaility = 0.05)
    public Message homoNum(User sender, MessageChain messageChain, Contact subject) {
        String num = messageChain.contentToString();
        if (!NumberUtil.isNumber(num)) {
            return null;
        }
        String homo = "";
        try {
            homo = homoLogic.homo(num);
            return new PlainText(homo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
