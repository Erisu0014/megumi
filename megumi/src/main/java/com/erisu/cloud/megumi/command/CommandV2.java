package com.erisu.cloud.megumi.command;

import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.util.Module;

import java.lang.annotation.*;

/**
 * 指令系统基本配置
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandV2 {

    CommandType commandType();

    Pattern pattern();

    String value() default "";

    Module module() default Module.DEFAULT;

    String[] alias() default {};

//    boolean isSpec() default false;// 是否特殊化处理

    String uuid() default "";

    /**
     * 概率回复函数
     * @return
     */
//    Probability probaility(); 没想好怎么实现更优雅

}
