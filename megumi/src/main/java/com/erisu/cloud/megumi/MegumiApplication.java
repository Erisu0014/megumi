package com.erisu.cloud.megumi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class MegumiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegumiApplication.class, args);
    }

}
