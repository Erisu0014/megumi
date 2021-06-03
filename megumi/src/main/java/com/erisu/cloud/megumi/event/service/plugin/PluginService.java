package com.erisu.cloud.megumi.event.service.plugin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.CommandV2;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.event.service.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.event.service.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.event.service.plugin.pojo.Model;
import com.erisu.cloud.megumi.event.service.plugin.pojo.Plugin;
import com.erisu.cloud.megumi.pattern.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description plugin test
 * @Author alice
 * @Date 2021/1/7 16:56
 **/
@Slf4j
@Component
@Model(name = "plugin")
public class PluginService implements ICommandService {
    @Resource
    private PluginLogic pluginLogic;


    @CommandV2(commandType = CommandType.GROUP, pattern = Pattern.PREFIX, value = "启用 ", alias = {"停用 "})
    public Message execute(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group group = (Group) subject;
        String content = ((PlainText) messageChain.get(1)).getContent();
        int enabled;
        String pluginName;
        if (StrUtil.startWith(content, "启用 ")) {
            pluginName = StrUtil.removePrefix(content, "启用 ");
            enabled = 1;
        } else if (StrUtil.startWith(content, "停用 ")) {
            pluginName = StrUtil.removePrefix(content, "停用 ");
            enabled = 0;
        } else {
            return new PlainText("你他妈怎么进来的");
        }
        List<Plugin> plugins = pluginLogic.getPluginByName(pluginName);
        if (CollUtil.isEmpty(plugins)) {
            return new PlainText("主人没写，我不会做的鸭");
        }
        pluginLogic.updateGroupPlugin(
                GroupPlugin.builder()
                        .groupId(String.valueOf(group.getId()))
                        .pluginId(plugins.get(0).getId()).enabled(enabled).build());
        return new PlainText("初始化插件成功");
    }
}
