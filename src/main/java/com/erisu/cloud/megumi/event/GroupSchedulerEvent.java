package com.erisu.cloud.megumi.event;

import com.erisu.cloud.megumi.event.annotation.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotActiveEvent;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.BotReloginEvent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 定时任务事件插件
 * @Author alice
 * @Date 2020/12/30 11:11
 **/
@Event
public class GroupSchedulerEvent extends SimpleListenerHost {

    /**
     * 什么bug，为什么进不来
     *
     * @param event
     * @return
     * @throws Exception
     */
    @EventHandler(ignoreCancelled = false)
    public ListeningStatus onBotOnline(BotOnlineEvent event) throws Exception {
        event.getBot().getGroup(604515343).sendMessage(new PlainText("没人理我，走了"));
        return ListeningStatus.LISTENING;
    }

//    @EventHandler(priority = Listener.EventPriority.NORMAL)
//    public ListeningStatus onBotRelogin(BotReloginEvent event) throws Exception {
////        event.getBot().getGroup(604515343).sendMessage(new PlainText(""));
//        return ListeningStatus.LISTENING;
//    }
//
//    @EventHandler(priority = Listener.EventPriority.NORMAL)
//    public ListeningStatus onBotActive(BotActiveEvent event) throws Exception {
////        event.getBot().getGroup(604515343).sendMessage(new PlainText(""));
//        return ListeningStatus.LISTENING;
//    }
}
