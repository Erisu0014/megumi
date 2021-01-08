package com.erisu.cloud.megumi.scheduler;

import cn.hutool.core.collection.CollUtil;
import com.erisu.cloud.megumi.event.service.plugin.logic.PluginLogic;
import com.erisu.cloud.megumi.event.service.plugin.pojo.GroupPlugin;
import net.mamoe.mirai.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author alice
 * @Date 2021/1/7 17:18
 **/
@Component
public class BaseScheduler {
    @Value("${qq.username}")
    private long username;
    @Resource
    private PluginLogic pluginLogic;

    @Scheduled(cron = "0/30 * * * * ?") //每30s执行一次
    public void testScheduler() throws Exception {
        List<GroupPlugin> plugins = pluginLogic.getGroupPluginByName("jap");
        if (CollUtil.isNotEmpty(plugins)) {
            for (GroupPlugin plugin : plugins) {
                if (Bot.getInstance(username).getGroups().size() != 0) {
                    Bot.getInstance(username).getGroup(Long.parseLong(plugin.getGroupId())).sendMessage("えへってなんだよ");
                }
            }
        }
    }
}
