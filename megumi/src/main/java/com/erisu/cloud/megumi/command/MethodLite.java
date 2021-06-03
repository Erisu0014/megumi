package com.erisu.cloud.megumi.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * @Description TODO
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
