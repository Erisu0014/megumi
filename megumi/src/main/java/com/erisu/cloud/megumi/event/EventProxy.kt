package com.erisu.cloud.megumi.event

import com.erisu.cloud.megumi.analysis.handler.AnalysisHandler
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.GlobalCommands
import com.erisu.cloud.megumi.command.MethodLite
import com.erisu.cloud.megumi.plugin.pojo.Model
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Lazy
import javax.annotation.PostConstruct
import javax.annotation.Resource
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

/**
 *@Description TODO
 *@Author alice
 *@Date 2021/6/25 9:08
 **/
@Slf4j
@Lazy
@Event
class EventProxy : SimpleListenerHost() {
    @Value("\${qq.username}")
    private val username: Long = 0

    @Autowired
    private val applicationContext: ApplicationContext? = null

    @Resource
    private val analysisHandler: AnalysisHandler? = null

    @PostConstruct
    fun register() {
        val beansWithAnnotation = applicationContext!!.getBeansWithAnnotation(
            Model::class.java)
        val beansV2: MutableMap<Command, MethodLite> = HashMap()
        //        List<Command> commands = new ArrayList<>();
        beansWithAnnotation.forEach { (k: String?, v: Any) ->
            if (v.javaClass.getAnnotation(
                    Model::class.java).isEnabled
            ) {
                val methods = v.javaClass.declaredMethods
                for (method in methods) {
                    if (method.getAnnotation(Command::class.java) != null) {
                        beansV2[method.getAnnotation(Command::class.java)] =
                            MethodLite(method, v)
                    }
                }
            }
        }
        GlobalCommands.commands = beansV2
//        print("command指令初始化完成")
    }

    @EventHandler(priority = EventPriority.NORMAL)
    @Throws(Exception::class)
    suspend fun executeCommand(messageEvent: MessageEvent): ListeningStatus {
        val methodLites = analysisHandler!!.verify(messageEvent)
        for ((method, bean) in methodLites) {
            var answer: Any? = null
            val command = method.getAnnotation(Command::class.java)
            if (Math.random() > command.probaility) return ListeningStatus.LISTENING
            //  随机概率小于设定概率，执行
            try {
                val a = method.kotlinFunction
                answer = if (a!!.isSuspend) {
                    a.callSuspend(bean, messageEvent.sender, messageEvent.message, messageEvent.subject)
                } else {
                    a.call(bean, messageEvent.sender, messageEvent.message, messageEvent.subject)
                }
//                answer = method.invoke(bean, messageEvent.sender, messageEvent.message, messageEvent.subject)
            } catch (e: Exception) {
                handleException(e, messageEvent)
            }
            // 答案为数组且不为空
            if (answer is List<*>) answer.forEach {
                when {
                    it is Message && it !is EmptyMessageChain -> {
                        messageEvent.subject.sendMessage(it)
                    }
                }
            } else if (answer !is Message || answer is EmptyMessageChain) continue
            //            Message answer = service.execute(messageEvent.getSender(), messageEvent.getMessage(), messageEvent.getSubject());
            else messageEvent.subject.sendMessage(answer)
        }
        return ListeningStatus.LISTENING
    }

    private suspend fun handleException(e: Throwable, event: MessageEvent) {
        e.printStackTrace()
        event.subject.sendMessage("唔，出问题了，联系爱丽丝姐姐看看吧")
    }
}