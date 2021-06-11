package com.erisu.cloud.megumi.arena.service;

import cn.hutool.core.collection.CollUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @Description jjc/pjjc相关，提醒模块：arena
 * @Author alice
 * @Date 2021/6/11 15:39
 **/
@Component
public class ArenaRemindService {
    @Value("${qq.username}")
    private long username;
    @Resource
    private PluginLogic pluginLogic;

    @Scheduled(cron = "00 50 13 * * ?")
    public void remindArena() throws Exception {
        List<GroupPlugin> plugins = pluginLogic.getGroupPluginByName("arena", null);
        if (CollUtil.isNotEmpty(plugins)) {
            Bot bot = Bot.getInstance(username);
            File nico = ResourceUtils.getFile("classpath:emoticon/fencing.jpg");
            ExternalResource externalResource = ExternalResource.create(nico);
            for (GroupPlugin plugin : plugins) {
                if (plugin.getEnabled() > 0 && Bot.getInstance(username).getGroups().size() != 0) {
                    long groupId = Long.parseLong(plugin.getGroupId());
                    Image image = Objects.requireNonNull(bot.getGroup(groupId)).uploadImage(externalResource);
                    Objects.requireNonNull(bot.getGroup(groupId)).sendMessage(image);
                }
            }
        }

    }


}
