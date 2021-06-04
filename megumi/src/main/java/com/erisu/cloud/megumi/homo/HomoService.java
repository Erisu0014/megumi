package com.erisu.cloud.megumi.homo;

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

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CHECK)
    public Message homoNum(User sender, MessageChain messageChain, Contact subject) throws Exception {
        String homo = homoLogic.homo(((PlainText) messageChain.get(1)).getContent());
        return new PlainText(homo);
    }
}
