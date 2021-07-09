package com.erisu.cloud.megumi.event

import cn.hutool.core.lang.UUID
import com.erisu.cloud.megumi.analysis.handler.AnalysisHandler
import com.erisu.cloud.megumi.command.Command
import com.erisu.cloud.megumi.command.GlobalCommands
import com.erisu.cloud.megumi.command.MethodLite
import com.erisu.cloud.megumi.plugin.pojo.Model
import com.erisu.cloud.megumi.util.MessageModel
import com.erisu.cloud.megumi.util.MessageUtil
import com.erisu.cloud.megumi.util.PythonRunner
import com.erisu.cloud.megumi.util.RedisUtil
import lombok.extern.slf4j.Slf4j
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Lazy
import java.io.File
import javax.annotation.PostConstruct
import javax.annotation.Resource
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

    @Resource
    private lateinit var redisUtil: RedisUtil

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Resource
    private lateinit var analysisHandler: AnalysisHandler

    @PostConstruct
    fun register() {
        val beansWithAnnotation = applicationContext.getBeansWithAnnotation(Model::class.java)
        val beansV2: MutableMap<Command, MethodLite> = HashMap()
        //        List<Command> commands = new ArrayList<>();
        beansWithAnnotation.forEach { (_: String?, v: Any) ->
            if (v.javaClass.getAnnotation(Model::class.java).isEnabled) {
                val methods = v.javaClass.declaredMethods
                methods.forEach { method ->
                    if (method.getAnnotation(Command::class.java) != null) {
                        beansV2[method.getAnnotation(Command::class.java)] = MethodLite(method, v)
                    }
                }
            }
        }
        GlobalCommands.commands = beansV2
    }

    @EventHandler(priority = EventPriority.NORMAL)
    @Throws(Exception::class)
    suspend fun executeCommand(messageEvent: MessageEvent): ListeningStatus {
        val methodLites = analysisHandler.verify(messageEvent)
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
//               answer = method.invoke(bean, messageEvent.sender, messageEvent.message, messageEvent.subject)
            } catch (e: Exception) {
                handleException(e, messageEvent)
            }
            if (answer !is Message || answer is EmptyMessageChain) continue
            else messageEvent.subject.sendMessage(buildMessage(messageEvent.subject as Group, answer))
        }
        return ListeningStatus.LISTENING
    }

    private suspend fun buildMessage(group: Group, message: Message): Message {
        val model = redisUtil.get("model")
        val cachePath = "${System.getProperty("user.dir")}${File.separator}cache"
        return if (message is PlainText && model.toInt() == MessageModel.IMAGE.type) {
            val uuid: String = UUID.fastUUID().toString(true)
            PythonRunner.runPythonScript(
                "${System.getProperty("user.dir")}${File.separator}script${File.separator}text_to_image.py",
                arrayOf(message.contentToString(), cachePath, "${uuid}.jpg")
            )
            MessageUtil.generateImage(
                group,
                File("${cachePath}${File.separator}${uuid}.jpg"), true
            )
        } else {
            message
        }
    }

    private fun handleException(e: Throwable, event: MessageEvent) {
        e.printStackTrace()
//        event.subject.sendMessage("唔，出问题了，联系爱丽丝姐姐看看吧")
    }
}