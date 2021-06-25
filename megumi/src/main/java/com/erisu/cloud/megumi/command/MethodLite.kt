package com.erisu.cloud.megumi.command

import java.lang.reflect.Method

/**
 * @Description 用以方法反射, bean为注入至ioc中bean的节点
 * @Author alice
 * @Date 2021/6/3 19:42
 **/
data class MethodLite(
    val method: Method,
    val bean: Any,
)


