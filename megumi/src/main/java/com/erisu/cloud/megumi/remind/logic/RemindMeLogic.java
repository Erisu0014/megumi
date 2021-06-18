package com.erisu.cloud.megumi.remind.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erisu.cloud.megumi.remind.mapper.DevRemindMapper;
import com.erisu.cloud.megumi.remind.pojo.DevRemind;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author alice
 * @Date 2021/6/15 9:01
 **/
@Component
public class RemindMeLogic {
    @Resource
    private DevRemindMapper remindMapper;

    public void addRemindMe(DevRemind remind) {
        remindMapper.insert(remind);
    }

    public Message getRemindMe(String groupId, String senderId) {
        QueryWrapper<DevRemind> remindQueryWrapper = new QueryWrapper<>();
        remindQueryWrapper.eq("group_id", groupId);
        remindQueryWrapper.eq("qq_id", senderId);
        List<DevRemind> devReminds = remindMapper.selectList(remindQueryWrapper);
        MessageChainBuilder builder = new MessageChainBuilder();
//        final int[] index = {1};
        devReminds.forEach(r -> {
            MessageChain messages = MessageChain.deserializeFromJsonString(r.getRemindJson());
            builder.append("☑️　messageId-")
                    .append(String.valueOf(r.getId())).append(":\n").append(messages).append("\n");
//            index[0]++;
        });
        return builder.build();
    }

    /**
     * @param remindMeId
     * @param groupId
     * @param userId
     * @return
     */
    public Message removeRemindMe(String remindMeId, String groupId, String userId) {
        QueryWrapper<DevRemind> remindQueryWrapper = new QueryWrapper<>();
        remindQueryWrapper.eq("group_id", groupId);
        remindQueryWrapper.eq("qq_id", userId);
        remindQueryWrapper.eq("id", remindMeId);
        if (remindMapper.delete(remindQueryWrapper) > 0) {
            return new PlainText("删除成功");
        } else {
            return new PlainText("删除失败");
        }
    }
}
