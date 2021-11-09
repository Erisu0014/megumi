package com.erisu.cloud.megumi.remind.logic

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.erisu.cloud.megumi.remind.mapper.DevRemindMapper
import com.erisu.cloud.megumi.remind.pojo.DevRemind
import com.erisu.cloud.megumi.util.StreamMessageUtil
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.*
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.function.Consumer
import javax.annotation.Resource

/**
 *@Description remindMe相关逻辑
 *@Author alice
 *@Date 2021/6/23 16:48
 **/
@Component
class RemindMeLogic {
    @Resource
    private lateinit var remindMapper: DevRemindMapper

    fun addRemindMe(remind: DevRemind) {
        remindMapper.insert(remind)
    }

    fun getRemindMe(groupId: String?, senderId: String?): Message {
        val remindQueryWrapper = QueryWrapper<DevRemind>()
        remindQueryWrapper.eq("group_id", groupId).eq("qq_id", senderId)
        val devReminds = remindMapper.selectList(remindQueryWrapper)
        val messageChain = MessageChainBuilder()
        devReminds.forEach(Consumer { (id, _, _, remindJson) ->
            val messages = MessageChain.deserializeFromJsonString(remindJson)
            messageChain.append("✅　messageId-")
                .append(id.toString()).append(":\n").append(messages).append("\n")
        })
        return messageChain.build()
    }

    /**
     * @param remindMeId
     * @param groupId
     * @param userId
     * @return
     */
    suspend fun removeRemindMe(remindMeId: String?, group: Group, userId: String?): Message {
        val remindQueryWrapper = QueryWrapper<DevRemind>()
        remindQueryWrapper.eq("group_id", group.id).eq("qq_id", userId).eq("id", remindMeId)
        val resultList = remindMapper.selectList(remindQueryWrapper)
        val image = StreamMessageUtil.generateImage(group, ClassPathResource("emoticon/露娜笑.jpg").inputStream)
        return if (remindMapper.delete(remindQueryWrapper) > 0)
            messageChainOf(PlainText("完成了${resultList[0].remindJson}真了不起~"), image)
        else PlainText("删除失败")
    }

}