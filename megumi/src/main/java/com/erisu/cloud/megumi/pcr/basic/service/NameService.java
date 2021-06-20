package com.erisu.cloud.megumi.pcr.basic.service;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.pcr.basic.logic.PcrInitData;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

@Model(name = "name")
@Component
public class NameService {
    @Command(commandType = CommandType.GROUP, value = "谁是", pattern = Pattern.PREFIX, uuid = "d92fcb894ba34f81a108194e219999bb")
    public Message searchName(User sender, MessageChain messageChain, Contact subject) throws Exception {
        PlainText plainText = (PlainText) messageChain.get(1);
        String name = StrUtil.removePrefix(plainText.getContent().trim(), "谁是").trim();
        PcrInitData.INSTANCE.initData();
        String value = PcrInitData.INSTANCE.getNameMap().get(name);
        if (value == null) {
            return new PlainText(String.format("兰德索尔似乎没有叫%s的人...", name));
        } else {
            return new PlainText(String.format("序号：%s", value));
        }
    }
}
