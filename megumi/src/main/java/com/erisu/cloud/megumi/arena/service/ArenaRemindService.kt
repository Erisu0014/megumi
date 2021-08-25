package com.erisu.cloud.megumi.arena.service

import com.erisu.cloud.megumi.plugin.logic.PluginLogic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 *@Description jjc/pjjc相关，提醒模块：arena
 *@Author alice
 *@Date 2021/6/23 18:23
 **/
@Component
class ArenaRemindService {
    @Value("\${qq.username}")
    private val username: Long = 0

    @Resource
    private lateinit var pluginLogic: PluginLogic

//    @Scheduled(cron = "00 50 13 * * ?")
//    @Throws(FileNotFoundException::class)
//    fun remindArena() {
//        GlobalScope.future {
//            val plugins = pluginLogic.getGroupPluginByName("arena", null)
//            if (CollUtil.isNotEmpty(plugins)) {
//                val bot = Bot.getInstance(username)
//                val nico = withContext(Dispatchers.IO) { ClassPathResource("emoticon/fencing.jpg").inputStream }
//                for (plugin in plugins) {
//                    if (plugin.enabled > 0 && Bot.getInstance(username).groups.isNotEmpty()) {
//                        val groupId = plugin.groupId.toLong()
//                        val group = bot.getGroup(groupId) as Group
//                        val message = StreamMessageUtil.generateImage(group, nico)
//                        group.sendMessage(message)
//                    }
//                }
//            }
//        }
//    }
}


