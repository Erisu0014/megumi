package com.erisu.cloud.megumi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@PropertySource("classpath:mysql.properties")
@PropertySource("classpath:redis.properties")
@PropertySource("classpath:qq.properties")
@PropertySource("classpath:other.properties")
@EnableAsync
@SpringBootApplication
public class MegumiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegumiApplication.class, args);
    }

}
