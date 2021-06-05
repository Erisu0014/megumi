package com.erisu.cloud.megumi.plugin.pojo;

import java.lang.annotation.*;

/**
 * 其实就是数据库里的plugin[名字一致性还在想]
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Model {
    String name() default "";

    String uuid() default "";

    // TODO: 2021/6/5 有关model enabled和数据库一致性问题待考虑
    boolean isEnabled() default true;
}
