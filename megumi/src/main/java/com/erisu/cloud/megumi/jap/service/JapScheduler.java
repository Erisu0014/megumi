package com.erisu.cloud.megumi.jap.service;

import cn.hutool.core.collection.CollUtil;
import com.erisu.cloud.megumi.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.jap.logic.JapLogic;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description jap组件的使用
 * @Author alice
 * @Date 2021/1/11 10:53
 **/
@Component
public class JapScheduler {
    @Value("${qq.username}")
    private long username;
    @Resource
    private PluginLogic pluginLogic;
    @Resource
    private JapLogic japLogic;

    @Scheduled(cron = "0 20 10 * * ?")
    public void testScheduler() throws Exception {
        List<GroupPlugin> plugins = pluginLogic.getGroupPluginByName("jap", null);
        if (CollUtil.isNotEmpty(plugins)) {
            for (GroupPlugin plugin : plugins) {
                if (plugin.getEnabled() > 0 && Bot.getInstance(username).getGroups().size() != 0) {
                    Message message = japLogic.TodayMessageBuilder();
                    Bot.getInstance(username).getGroup(Long.parseLong(plugin.getGroupId())).sendMessage(message);
                }
            }
        }
    }
}
