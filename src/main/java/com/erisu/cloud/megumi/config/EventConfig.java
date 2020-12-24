package com.erisu.cloud.megumi.config;

import com.erisu.cloud.megumi.event.EventProxy;
import com.erisu.cloud.megumi.event.GroupEvent;
import net.mamoe.mirai.event.ListenerHost;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 事件注册器
 * @Author alice
 * @Date 2020/11/18 18:52
 **/
@Configuration
public class EventConfig {
    @Resource
    private GroupEvent groupEvent;
    @Resource
    private EventProxy eventProxy;


    @Bean(name = "megumiEvent")
    public List<ListenerHost> getMegumiEvents() {
        List<ListenerHost> events = new ArrayList<>();
        events.add(groupEvent);
        events.add(eventProxy);
        return events;
    }

}
