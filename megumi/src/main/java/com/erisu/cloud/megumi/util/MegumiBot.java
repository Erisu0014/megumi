package com.erisu.cloud.megumi.util;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;


/**
 * @Description bot po
 * @Author alice
 * @Date 2020/12/4 10:38
 **/
@Slf4j
public class MegumiBot {
    @Value("${qq.username}")
    private static long username;


    public static void startBot(Long account, String pwd, String deviceInfo, List<ListenerHost> events) {
        BotConfiguration config = new BotConfiguration();
        config.fileBasedDeviceInfo(deviceInfo);
        config.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        config.setNetworkLoggerSupplier(Bot::getLogger);
        config.redirectNetworkLogToDirectory(new File("logs"));
        final Bot megumi = BotFactory.INSTANCE.newBot(account, pwd, config);
        megumi.login();
        // 注册事件
        for (ListenerHost event : events) {
            GlobalEventChannel.INSTANCE.registerListenerHost(event);
//            GlobalEventChannel.INSTANCE.exceptionHandler(e -> {
//                if (e instanceof MegumiException) {
//                    MegumiException e1 = (MegumiException) e;
//                    Bot bot = Bot.getInstance(username);
//                    Contact subject = e1.getSubject();
//                    if (subject instanceof Group) {
//                        Objects.requireNonNull(bot.getGroup(subject.getId())).sendMessage(e1.getReturnMsg());
//                    }
//
//                }
//                return Unit.INSTANCE;
//            });
            new Thread(megumi::join).start();
        }
    }
}
