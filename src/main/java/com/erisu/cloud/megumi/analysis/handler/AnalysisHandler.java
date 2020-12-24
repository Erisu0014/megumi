package com.erisu.cloud.megumi.analysis.handler;

import com.erisu.cloud.megumi.analysis.annotation.PreAnalysis;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.GlobalCommands;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.MessageEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description 前置分析handler
 * @Author alice
 * @Date 2020/12/24 16:26
 **/
@Slf4j
@Component
@Aspect
public class AnalysisHandler {

    @Pointcut("@annotation(com.erisu.cloud.megumi.analysis.annotation.PreAnalysis)")
    public void preAnalysis() {

    }

    @Before("@annotation(preAnalysis)")
    public void verify(JoinPoint point, PreAnalysis preAnalysis) throws Exception {
        Object[] args = point.getArgs();
        if (args[0] == null) {
            throw new Exception("有空改成全局异常处理");
        }
        MessageEvent messageEvent = (MessageEvent) args[0];
        Map<Command, Object> commandObjectMap = GlobalCommands.COMMANDS;


    }


}
