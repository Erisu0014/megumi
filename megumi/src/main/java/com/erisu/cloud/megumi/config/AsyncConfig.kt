package com.erisu.cloud.megumi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

/**
 *@Description 使用异步任务
 *@Author alice
 *@Date 2021/11/3 9:39
 **/
@Configuration
@EnableAsync
open class AsyncConfig {
    @Bean
    open fun taskExecutor(): Executor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 10
        taskExecutor.maxPoolSize = 20
        taskExecutor.setQueueCapacity(10)
        return taskExecutor
    }
}