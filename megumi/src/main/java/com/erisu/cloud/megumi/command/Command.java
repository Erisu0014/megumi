package com.erisu.cloud.megumi.command;

import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.util.Module;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 指令系统基本配置
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

    CommandType commandType();

    Pattern pattern();

    String value() default "";

    Module module() default Module.DEFAULT;

    String[] alias() default {};

    /**
     * 概率回复函数
     * @return
     */
//    Probability probaility(); 没想好怎么实现更优雅

}
