//package com.erisu.cloud.megumi.plugin;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.StrUtil;
//import com.erisu.cloud.megumi.command.Command;
//import com.erisu.cloud.megumi.command.CommandType;
//import com.erisu.cloud.megumi.pattern.Pattern;
//import com.erisu.cloud.megumi.plugin.logic.PluginLogic;
//import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
//import com.erisu.cloud.megumi.plugin.pojo.Model;
//import com.erisu.cloud.megumi.plugin.pojo.Plugin;
//import lombok.extern.slf4j.Slf4j;
//import net.mamoe.mirai.contact.Contact;
//import net.mamoe.mirai.contact.Group;
//import net.mamoe.mirai.contact.User;
//import net.mamoe.mirai.message.data.Message;
//import net.mamoe.mirai.message.data.MessageChain;
//import net.mamoe.mirai.message.data.PlainText;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @Description plugin test
// * @Author alice
// * @Date 2021/1/7 16:56
// **/
//@Slf4j
//@Component
//@Model(name = "plugin")
//public class PluginService {
//    @Resource
//    private PluginLogic pluginLogic;
//
//
//    @Command(commandType = CommandType.GROUP, pattern = Pattern.PREFIX, value = "启用", alias = {"停用"})
//    public Message executePlugin(User sender, MessageChain messageChain, Contact subject) {
//        Group group = (Group) subject;
//        String content = ((PlainText) messageChain.get(1)).getContent();
//        int enabled;
//        String pluginName;
//        if (StrUtil.startWith(content, "启用")) {
//            pluginName = StrUtil.removePrefix(content, "启用").trim();
//            enabled = 1;
//        } else if (StrUtil.startWith(content, "停用")) {
//            pluginName = StrUtil.removePrefix(content, "停用").trim();
//            enabled = 0;
//        } else {
//            return new PlainText("你他妈怎么进来的");
//        }
//        List<Plugin> plugins = pluginLogic.getPluginByName(pluginName);
//        if (CollUtil.isEmpty(plugins)) {
//            return new PlainText("主人没写，我不会做的鸭");
//        }
//        pluginLogic.updateGroupPlugin(new GroupPlugin(String.valueOf(group.getId()), plugins.get(0).getId(), enabled));
//        return new PlainText("初始化插件成功");
//    }
//}
