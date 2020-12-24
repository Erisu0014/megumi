package com.erisu.cloud.megumi.event;

import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandService;
import com.erisu.cloud.megumi.command.GlobalCommands;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.event.annotation.Event;
import com.erisu.cloud.megumi.analysis.annotation.PreAnalysis;
import com.erisu.cloud.megumi.hello.HelloService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 事件proxy
 * @Author alice
 * @Date 2020/12/9 16:09
 **/

@Slf4j
@Lazy
@Event
public class EventProxy extends SimpleListenerHost {
    @Autowired
    private ApplicationContext applicationContext;
    @Resource
    private CommandService commandService;


    @PostConstruct
    public void register() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Command.class);
        Map<Command, Object> beans = new HashMap<>();
//        List<Command> commands = new ArrayList<>();
        beansWithAnnotation.forEach((k, v) -> {
            Command command = v.getClass().getAnnotation(Command.class);
            beans.put(command, v);
//            commands.add(command);
        });
        GlobalCommands.COMMANDS = beans;
        log.info("command指令初始化完成");
    }

    @PreAnalysis
    @EventHandler(priority = Listener.EventPriority.NORMAL)
    public ListeningStatus execute(MessageEvent messageEvent) throws Exception {
        ICommandService service = commandService.getCommandService();
        Message answer = service.execute(messageEvent.getSender(), messageEvent.getMessage(), messageEvent.getSubject());
        if (answer != null) {
            messageEvent.getSubject().sendMessage(answer);
        }
        return ListeningStatus.LISTENING;
    }



}
