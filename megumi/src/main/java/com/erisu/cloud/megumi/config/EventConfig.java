package com.erisu.cloud.megumi.config;

import com.erisu.cloud.megumi.event.EventProxy;
import com.erisu.cloud.megumi.event.BaseGroupEvent;
import com.erisu.cloud.megumi.event.GroupSchedulerEvent;
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
    private BaseGroupEvent baseGroupEvent;
    @Resource
    private EventProxy eventProxy;
    @Resource
    private GroupSchedulerEvent groupSchedulerEvent;


    @Bean(name = "megumiEvent")
    public List<ListenerHost> getMegumiEvents() {
        List<ListenerHost> events = new ArrayList<>();
        events.add(baseGroupEvent);
        events.add(eventProxy);
        events.add(groupSchedulerEvent);
        return events;
    }

}
