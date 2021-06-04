package com.erisu.cloud.megumi.util;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;
import java.util.List;


/**
 * @Description bot po
 * @Author alice
 * @Date 2020/12/4 10:38
 **/
public class MegumiBot {
    public static void startBot(Long account, String pwd, String deviceInfo, List<ListenerHost> events) {
        BotConfiguration config = new BotConfiguration();
        config.fileBasedDeviceInfo(deviceInfo);
        config.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
        config.setNetworkLoggerSupplier(Bot::getLogger);
        config.redirectNetworkLogToDirectory(new File("logs"));
        final Bot megumi = BotFactory.INSTANCE.newBot(account, pwd, config);
        megumi.login();
        // 注册事件
        for (ListenerHost event : events) {
            GlobalEventChannel.INSTANCE.registerListenerHost(event);
        }
        new Thread(megumi::join).start();
    }
}
