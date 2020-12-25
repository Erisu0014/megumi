package com.erisu.cloud.megumi.analysis.handler;

import com.erisu.cloud.megumi.analysis.annotation.PreAnalysis;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.command.GlobalCommands;
import com.erisu.cloud.megumi.command.ICommandService;
import com.erisu.cloud.megumi.pattern.PatternFactory;
import com.erisu.cloud.megumi.pattern.PatternStrategy;
import com.erisu.cloud.megumi.pattern.strategy.ContainsStrategy;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    public List<ICommandService> verify(MessageEvent messageEvent) throws Exception {
        Message message = messageEvent.getMessage();
        Contact subject = messageEvent.getSubject();
        boolean isGroup = messageEvent instanceof GroupMessageEvent;
        Map<Command, Object> commandObjectMap = GlobalCommands.commands;
        // 满意度commands
        List<ICommandService> commandServices = new ArrayList<>();
        commandObjectMap.forEach((command, v) -> {
            if (isGroup && command.commandType().equals(CommandType.GROUP)) {
                PatternStrategy processorHandler = PatternFactory.getProcessorHandler(command.pattern());
                if (processorHandler.isMatch(message, command.value())) {
                    commandServices.add((ICommandService) v);
                }
            }
        });
        return commandServices;
    }


}
