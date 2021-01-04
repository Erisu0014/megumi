package com.erisu.cloud.megumi.pattern;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PatternSupport {

    String name() default "";

    Pattern pattern(); //暂时不支持多pattern匹配，后面有需求再说吧
}
