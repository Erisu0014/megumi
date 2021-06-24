package com.erisu.cloud.megumi.event

import org.springframework.stereotype.Component

/**
 * 事件注册工具
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Component
@MustBeDocumented
annotation class Event
