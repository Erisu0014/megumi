package com.erisu.cloud.megumi.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 全局commands
 * @Author alice
 * @Date 2020/12/24 16:59
 **/
public class GlobalCommands {
    public static Map<Command, MethodLite> commands = new ConcurrentHashMap<>();
}
