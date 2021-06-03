package com.erisu.cloud.megumi.event;

import cn.hutool.core.util.IdUtil;
import com.erisu.cloud.megumi.analysis.annotation.PreAnalysis;
import com.erisu.cloud.megumi.analysis.handler.AnalysisHandler;
import com.erisu.cloud.megumi.command.*;
import com.erisu.cloud.megumi.event.annotation.Event;
import com.erisu.cloud.megumi.event.service.plugin.pojo.Model;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 事件proxy，主要是commands指令控制
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
    private AnalysisHandler analysisHandler;


    @PostConstruct
    public void register() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Model.class);
        Map<Command, Object> beans = new HashMap<>();
        Map<CommandV2, MethodLite> beansV2 = new HashMap<>();
//        List<Command> commands = new ArrayList<>();
        beansWithAnnotation.forEach((k, v) -> {
            Command command = v.getClass().getAnnotation(Command.class);
            beans.put(command, v);

            Method[] methods = v.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(CommandV2.class) != null) {
                    beansV2.put(method.getAnnotation(CommandV2.class), MethodLite.builder().method(method).bean(v).build());
                }
            }
//            commands.add(command);
        });
        GlobalCommands.commands = beansV2;
        log.info("command指令初始化完成");
    }

    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus excuteCommand(MessageEvent messageEvent) throws Exception {
        List<MethodLite> methodLites = analysisHandler.verify(messageEvent);
        for (MethodLite methodLite : methodLites) {
            Method method = methodLite.getMethod();
            Object bean = methodLite.getBean();
            Object answer = method.invoke(bean, messageEvent.getSender(), messageEvent.getMessage(), messageEvent.getSubject());
            if (!(answer instanceof Message)) {
                continue;
            }
            Message final_answer = (Message) answer;
//            Message answer = service.execute(messageEvent.getSender(), messageEvent.getMessage(), messageEvent.getSubject());
            messageEvent.getSubject().sendMessage(final_answer);
        }
        return ListeningStatus.LISTENING;
    }
}
