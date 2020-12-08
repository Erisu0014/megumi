package com.erisu.cloud.megumi.command.common;

import java.lang.annotation.*;

/**
 * 指令系统基本配置
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

    CommandType commandType();

    Pattern pattern();

    /**
     * 概率回复函数
     * @return
     */
//    Probability probaility(); 没想好怎么实现更优雅

}
