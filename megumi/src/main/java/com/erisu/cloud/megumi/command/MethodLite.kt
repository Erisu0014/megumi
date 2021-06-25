package com.erisu.cloud.megumi.command

import net.mamoe.mirai.message.data.Message
import java.lang.reflect.Method
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.callSuspendBy

/**
 * @Description 用以方法反射, bean为注入至ioc中bean的节点
 * @Author alice
 * @Date 2021/6/3 19:42
 **/
data class MethodLite(
    val method: Method,
    val bean: Any,
)


