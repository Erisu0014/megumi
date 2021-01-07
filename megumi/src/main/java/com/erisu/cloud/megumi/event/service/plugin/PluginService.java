package com.erisu.cloud.megumi.event.service.plugin;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.util.PluginUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

/**
 * @Description plugin test
 * @Author alice
 * @Date 2021/1/7 16:56
 **/
@Slf4j
@Component
@Command(commandType = CommandType.GROUP, pattern = Pattern.PREFIX, value = "启用 ")
public class PluginService implements ICommandService {

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        String content = ((PlainText) messageChain.get(1)).getContent();
        String pluginName = StrUtil.removePrefix(content, "启用 ");
        if (!pluginName.equals("jap")) {
            return new PlainText("主人没写，我不会做的鸭");
        }
        PluginUtil.plugins.put(pluginName, true);
        return new PlainText("初始化插件成功");
    }
}
