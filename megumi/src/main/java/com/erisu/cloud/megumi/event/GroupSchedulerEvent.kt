package com.erisu.cloud.megumi.event

import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.message.data.PlainText

/**
 *@Description 定时任务事件插件
 *@Author alice
 *@Date 2022/5/10 15:52
 **/
@Event
@Slf4j
class GroupSchedulerEvent : SimpleListenerHost() {
    //    /**
//     *
//     *
//     * @param event
//     * @return
//     */
    @EventHandler
    suspend fun onBotOnline(event: BotOnlineEvent): ListeningStatus {
        event.bot.getGroup(604515343)!!.sendMessage(PlainText("我来了我来了我来了"))
        return ListeningStatus.LISTENING
    }


//    @EventHandler(priority = EventPriority.NORMAL)
//    fun onBotActive(event: BotActiveEvent?): ListeningStatus? {
////        event.getBot().getGroup(604515343).sendMessage(new PlainText(""));
//        return ListeningStatus.LISTENING
//    }
}