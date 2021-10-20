package com.erisu.cloud.megumi.command

import com.erisu.cloud.megumi.pattern.Pattern

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Command(
    val commandType: CommandType, //  群聊....
    val pattern: Pattern, val value: String = "", //匹配值
    val alias: Array<String> = [],  //    boolean isSpec() default false;// 是否特殊化处理
    val uuid: String = "",
    val probaility: Double = 1.0, //概率随机函数

    val isRecalled: Boolean = false,//是否撤回消息
    val recallTime: Long = 0,// 撤回时间:s
    val prefix: String = "",//  前缀
)

