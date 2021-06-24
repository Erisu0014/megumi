package com.erisu.cloud.megumi.event;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.*;

import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.action.MemberNudge;
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
     *
     * @param event
     * @return
     */
    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus onMemberJoinEvent(@NotNull MemberJoinEvent event) {
        String name = event.getMember().getNameCard();
        event.getGroup().sendMessage(String.format("欢迎%s进群~", name));
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }


    /**
     * 戳出事了你负责吗
     *
     * @param event
     * @return
     */
    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus onNudgedEvent(@NotNull NudgeEvent event) {
        Bot bot = event.getBot();
        if (!(event.getFrom() instanceof NormalMember)) {
            return ListeningStatus.LISTENING; // 表示继续监听事件
        }
        NormalMember contact = (NormalMember) event.getFrom();
        Group group = contact.getGroup();
        MemberNudge nudge = contact.nudge();
        nudge.sendTo(group);
        if (Math.random() > 0.5) {
            bot.getGroup(contact.getGroup().getId()).sendMessage("戳出事了你负责吗");
        }
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }


}
