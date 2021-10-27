package com.erisu.cloud.megumi.plugin.pojo

/**
 * 其实就是数据库里的plugin[名字一致性还在想]
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Model(
    val name: String = "", val uuid: String = "",  // TODO: 2021/6/5 有关model enabled和数据库一致性问题待考虑
    val isEnabled: Boolean = true,  //  默认启用
    val help: String = "",
)