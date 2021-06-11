package com.erisu.cloud.megumi.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * @Description 用以方法反射, bean为注入至ioc中bean的节点
 * @Author alice
 * @Date 2021/6/3 19:42
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodLite {
    private Method method;
    private Object bean;
}
