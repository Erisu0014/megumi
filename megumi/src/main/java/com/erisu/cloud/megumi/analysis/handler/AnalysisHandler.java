package com.erisu.cloud.megumi.analysis.handler;

import com.erisu.cloud.megumi.command.*;
import com.erisu.cloud.megumi.pattern.PatternFactory;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 前置分析handler(aop切不到 ， 我人晕了)
 * @Author alice
 * @Date 2020/12/24 16:26
 **/
@Slf4j
@Component
//@Aspect
public class AnalysisHandler {
//    @Resource
//    private PatternFactory patternFactory;

//    @Pointcut("@annotation(com.erisu.cloud.megumi.analysis.annotation.PreAnalysis)")
//    public void preAnalysis() {
//
//    }
//    @Before("@annotation(preAnalysis)")

    public List<MethodLite> verify(MessageEvent messageEvent) throws Exception {
        MessageChain messageChain = messageEvent.getMessage();
        Contact subject = messageEvent.getSubject();
        boolean isGroup = messageEvent instanceof GroupMessageEvent;
        Map<Command, MethodLite> commandObjectMap = GlobalCommands.commands;
        // 满意度commands
        List<MethodLite> methodLites = new ArrayList<>();
        commandObjectMap.forEach((command, methodLite) -> {
            if (isGroup && command.commandType().equals(CommandType.GROUP)) {
                PatternStrategy processorHandler = PatternFactory.getProcessorHandler(command.pattern());
                if (processorHandler != null && processorHandler.isMatch(messageChain, command.prefix(),command.value(), command.alias())) {
                    methodLites.add(methodLite);
                }
            }
        });
        return methodLites;
    }


}
