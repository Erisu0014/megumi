package com.erisu.cloud.megumi.scheduler;

import com.erisu.cloud.megumi.util.PluginUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.ListeningStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author alice
 * @Date 2021/1/7 17:18
 **/
@Component
public class BaseScheduler {
    @Value("${qq.username}")
    private long username;

    @Scheduled(cron = "0/30 * * * * ?") //每30s执行一次
    public void testScheduler() throws Exception {
        if (!PluginUtil.plugins.isEmpty() && PluginUtil.plugins.containsKey("jap")) {
            if (Bot.getInstance(username).getGroups().size() != 0) {
                Bot.getInstance(username).getGroup(604515343).sendMessage("定时任务这么写也行？");
            }
        }
    }
}
