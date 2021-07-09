package com.erisu.cloud.megumi.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.events.BotActiveEvent;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.message.data.PlainText;

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
     */

    @EventHandler
    public ListeningStatus onBotOnline(BotOnlineEvent event) {
        event.getBot().getGroup(604515343).sendMessage(new PlainText("我来了我来了我来了"));
        return ListeningStatus.STOPPED;
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus onBotActive(BotActiveEvent event) {
//        event.getBot().getGroup(604515343).sendMessage(new PlainText(""));
        return ListeningStatus.LISTENING;
    }


}
