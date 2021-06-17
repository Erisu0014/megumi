package com.erisu.cloud.megumi.util;

import net.mamoe.mirai.event.ListenerHost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description bot启动相关
 * @Author alice
 * @Date 2020/11/18 8:24
 **/
//@PropertySource("classpath:qq.properties")
@Component
public class BotServer implements ApplicationRunner {

    @Value("${qq.username}")
    private long username;

    @Value("${qq.password}")
    private String password;

    @Qualifier("megumiEvent")
    @Resource
    private List<ListenerHost> events;



//    @Value("${log.path}")
//    String logs;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MegumiBot.startBot(username, password, "deviceInfo.json", events);
    }
}
