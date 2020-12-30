package com.erisu.cloud.megumi.event;

import com.erisu.cloud.megumi.event.annotation.Event;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotActiveEvent;
import net.mamoe.mirai.event.events.BotNudgedEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.action.Nudge;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 群组事件[主动监听发起]
 * @Author alice
 * @Date 2020/11/23 16:56
 **/
@Event
public class BaseGroupEvent extends SimpleListenerHost {

    /**
     * 入群事件
     * @param event
     * @return
     */
    @NotNull
    @EventHandler(priority = Listener.EventPriority.NORMAL)
    public ListeningStatus onMemberJoinEvent(@NotNull MemberJoinEvent event) {
        String name = event.getMember().getNameCard();
        event.getGroup().sendMessage(String.format("欢迎%s进群~", name));
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }

    /**
     * 戳出事了你负责吗
     * @param event
     * @return
     */
    @NotNull
    @EventHandler(priority = Listener.EventPriority.NORMAL)
    public ListeningStatus onNudgedEvent(@NotNull BotNudgedEvent event) {
        Bot bot = event.getBot();
        Member contact = (Member) event.getFrom();
        Nudge.Companion.sendNudge(contact.getGroup(), contact.nudge());
        bot.getGroup(contact.getGroup().getId()).sendMessage("欸嘿~戳不到");
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }



}
