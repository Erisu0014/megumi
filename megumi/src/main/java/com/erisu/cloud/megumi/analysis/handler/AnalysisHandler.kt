package com.erisu.cloud.megumi.analysis.handler

import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.CommandType
import com.erisu.cloud.megumi.command.GlobalCommands
import com.erisu.cloud.megumi.command.MethodLite
import com.erisu.cloud.megumi.pattern.PatternFactory
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 *@Description 前置分析handler
 *@Author alice
 *@Date 2022/4/25 16:09
 **/
@Slf4j
@Component
class AnalysisHandler {
    @Value("#{'\${botList}'.split(',')}")
    private lateinit var botList: MutableList<String>

    //    @Resource
    //    private PatternFactory patternFactory;
    //    @Pointcut("@annotation(com.erisu.cloud.megumi.analysis.annotation.PreAnalysis)")
    //    public void preAnalysis() {
    //
    //    }
    //    @Before("@annotation(preAnalysis)")
    @Throws(Exception::class)
    fun verify(messageEvent: MessageEvent): List<MethodLite> {
        val messageChain = messageEvent.message
        val isGroup = messageEvent is GroupMessageEvent
        val commandObjectMap = GlobalCommands.commands
        // 满意度commands
        val methodLites: MutableList<MethodLite> = ArrayList()
        commandObjectMap.forEach { (command: Command, methodLite: MethodLite) ->
            if (isGroup && command.commandType == CommandType.GROUP
                && !botList.contains(messageEvent.sender.id.toString())
            ) {   //不回复bot信息
                val processorHandler =
                    PatternFactory.getProcessorHandler(command.pattern)
                if (processorHandler != null && processorHandler.isMatch(messageChain,
                        command.prefix,
                        command.value,
                        *command.alias)
                ) {
                    methodLites.add(methodLite)
                }
            }
        }
        return methodLites
    }
}