package com.erisu.cloud.megumi.event;

import com.erisu.cloud.megumi.analysis.annotation.PreAnalysis;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.event.annotation.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 群组事件[主动监听发起]
 * @Author alice
 * @Date 2020/11/23 16:56
 **/
@Event
public class GroupEvent extends SimpleListenerHost {

    @NotNull
    @EventHandler(priority = Listener.EventPriority.NORMAL)
    public ListeningStatus onMemberJoinEvent(@NotNull MemberJoinEvent event) {
        String name = event.getMember().getNameCard();
        event.getGroup().sendMessage(String.format("欢迎%s进群~", name));
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }

//    //todo 该message应当做成指令系统的一部分，这么写也是一种hello world
//    @NotNull
//    @EventHandler(priority = Listener.EventPriority.NORMAL)
//    public ListeningStatus onRepeatEvent(@NotNull GroupMessageEvent event) {
//        Message message = event.getMessage();
//        if (Math.random() > 0.5) {
//            event.getGroup().sendMessage(message);
//        }
//        return ListeningStatus.LISTENING; // 表示继续监听事件
//    }

}
