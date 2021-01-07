package com.erisu.cloud.megumi.event;

import com.erisu.cloud.megumi.event.annotation.Event;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotActiveEvent;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.BotReloginEvent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Description 定时任务事件插件
 * @Author alice
 * @Date 2020/12/30 11:11
 **/
@Event
@Slf4j
public class GroupSchedulerEvent extends SimpleListenerHost {


    /**
     * 什么bug，为什么进不来login能进relogin，不看了
     *
     * @param event
     * @return
     * @throws Exception
     */

    @EventHandler
    public ListeningStatus onBotOnline(BotOnlineEvent event) throws Exception {
        event.getBot().getGroup(604515343).sendMessage(new PlainText("没人理我，走了"));
        return ListeningStatus.STOPPED;
    }



    @EventHandler(priority = Listener.EventPriority.NORMAL)
    public ListeningStatus onBotActive(BotActiveEvent event) throws Exception {
//        event.getBot().getGroup(604515343).sendMessage(new PlainText(""));
        return ListeningStatus.LISTENING;
    }


}
