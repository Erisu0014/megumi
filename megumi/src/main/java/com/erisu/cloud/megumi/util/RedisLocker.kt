package com.erisu.cloud.megumi.util

import net.mamoe.mirai.contact.Group
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 *@Description redis locker
 *@Author alice
 *@Date 2021/10/9 14:14
 **/
@Component
class RedisLocker {
    @Resource
    private lateinit var redisUtil: RedisUtil

    /**
     * 检查是否有redis并发锁，如果有触发等待信息，没有则加锁
     *
     * @param key
     * @param waitInfo
     */
    suspend fun setRedisLock(key: String, group: Group, waitInfo: String) {
        if (redisUtil.hasKey(key)) {
            group.sendMessage(waitInfo)
        }
        redisUtil.setEx(key, "1", 30, TimeUnit.SECONDS)
    }

}