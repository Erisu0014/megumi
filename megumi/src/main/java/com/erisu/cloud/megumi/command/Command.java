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
public @interface Command {

    CommandType commandType();  //  群聊....

    Pattern pattern();

    String value() default "";    //匹配值

    String[] alias() default {};

//    boolean isSpec() default false;// 是否特殊化处理

    String uuid() default "";

    /**
     * 概率回复函数
     *
     * @return
     */
    double probaility() default 1.0; //没想好怎么实现更优雅

}
