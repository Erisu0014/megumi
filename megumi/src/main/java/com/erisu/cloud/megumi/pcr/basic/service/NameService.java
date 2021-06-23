package com.erisu.cloud.megumi.pcr.basic.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pcr.basic.logic.NameLogic;
import com.erisu.cloud.megumi.pcr.basic.logic.PcrInitData;
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrPrincess;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.util.RedisKey;
import com.erisu.cloud.megumi.util.RedisUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Model(name = "name")
@Component
public class NameService {
    @Resource
    private NameLogic nameLogic;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PcrInitData pcrInitData;

    @Command(commandType = CommandType.GROUP, value = "谁是", pattern = Pattern.PREFIX, uuid = "d92fcb894ba34f81a108194e219999bb")
    public Message searchName(User sender, MessageChain messageChain, Contact subject) throws Exception {
        PlainText plainText = (PlainText) messageChain.get(1);
        String name = StrUtil.removePrefix(plainText.getContent().trim(), "谁是").trim();
//        PcrInitData.INSTANCE.initData();
        String princessId = pcrInitData.getNameMap().get(name);
        if (princessId == null) {
            return new PlainText(String.format("兰德索尔似乎没有叫%s的人...", name));
        } else {
            return nameLogic.getAvatar(sender, (Group) subject, princessId);
        }
    }

    /**
     * 向现有库添加昵称
     *
     * @param sender
     * @param messageChain
     * @param subject
     * @return
     * @throws Exception
     */
    @Command(commandType = CommandType.GROUP, value = "添加昵称", pattern = Pattern.PREFIX, uuid = "35eb96b99d944a6b9bcc3d066407f6f4")
    public Message addNickName(User sender, MessageChain messageChain, Contact subject) throws Exception {
        PlainText plainText = (PlainText) messageChain.get(1);
        String text = StrUtil.removePrefix(plainText.getContent().trim(), "添加昵称").trim();
        String[] names = text.split(" ");
        if (names.length != 2) {
            return null;
        }
        Map<String, String> nameMap = pcrInitData.getNameMap();
        String id = nameMap.get(names[0]);
        if (id != null) {
            if (!nameMap.containsKey(names[1])) {
                nameMap.put(names[1], id);
                redisUtil.hPut(RedisKey.PRINCESS_NAME.getKey(), names[1], id);
                return new PlainText("添加成功~");
            } else {
                return new PlainText("已经有人叫这个名字了哦");
            }
        } else {
            return null;
        }
    }


    @Command(commandType = CommandType.GROUP, value = "体检", pattern = Pattern.PREFIX)
    public Message checkProfile(User sender, MessageChain messageChain, Contact subject) throws Exception {
        PlainText plainText = (PlainText) messageChain.get(1);
        String name = StrUtil.removePrefix(plainText.getContent().trim(), "体检").trim();
//        PcrInitData.INSTANCE.initData();
        String character = pcrInitData.getNameMap().get(name);
        if (character == null) {
            return new PlainText("无体检数据");
        } else {
            PcrPrincess pcrPrincess = pcrInitData.getPrincessMap().get(character);
            return new PlainText(pcrPrincess != null ? JSON.toJSONString(pcrPrincess) : "无体检数据");
        }
    }
}
