package com.erisu.cloud.megumi.event;

import com.erisu.cloud.megumi.analysis.handler.AnalysisHandler;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.GlobalCommands;
import com.erisu.cloud.megumi.command.MethodLite;
import com.erisu.cloud.megumi.event.annotation.Event;
import com.erisu.cloud.megumi.exception.MegumiException;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 事件proxy，主要是commands指令控制
 * @Author alice
 * @Date 2020/12/9 16:09
 **/

@Slf4j
@Lazy
@Event
public class EventProxy extends SimpleListenerHost {
    @Value("${qq.username}")
    private long username;
    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private AnalysisHandler analysisHandler;


    @PostConstruct
    public void register() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Model.class);
        Map<Command, MethodLite> beansV2 = new HashMap<>();
//        List<Command> commands = new ArrayList<>();
        beansWithAnnotation.forEach((k, v) -> {
            if (v.getClass().getAnnotation(Model.class).isEnabled()) {
                Method[] methods = v.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getAnnotation(Command.class) != null) {
                        beansV2.put(method.getAnnotation(Command.class), MethodLite.builder().method(method).bean(v).build());
                    }
                }
            }

        });
        GlobalCommands.commands = beansV2;
        log.info("command指令初始化完成");
    }

    @NotNull
    @EventHandler(priority = EventPriority.NORMAL)
    public ListeningStatus executeCommand(MessageEvent messageEvent) throws Exception {
        List<MethodLite> methodLites = analysisHandler.verify(messageEvent);
        for (MethodLite methodLite : methodLites) {
            Method method = methodLite.getMethod();
            Object bean = methodLite.getBean();
            Object answer = null;
            try {
                answer = method.invoke(bean, messageEvent.getSender(), messageEvent.getMessage(), messageEvent.getSubject());
            } catch (Exception e) {
                handleException(e, messageEvent);
            }
            if (!(answer instanceof Message)) {
                continue;
            }
            Message final_answer = (Message) answer;
//            Message answer = service.execute(messageEvent.getSender(), messageEvent.getMessage(), messageEvent.getSubject());
            messageEvent.getSubject().sendMessage(final_answer);
        }
        return ListeningStatus.LISTENING;
    }

    public void handleException(@NotNull Throwable e, MessageEvent event) {
        e.printStackTrace();
        event.getSubject().sendMessage("唔，出问题了，联系爱丽丝姐姐看看吧");
    }
}
