package com.erisu.cloud.megumi.event.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 事件注册工具
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Event {
}
