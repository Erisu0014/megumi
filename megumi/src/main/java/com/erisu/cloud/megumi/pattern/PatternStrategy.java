package com.erisu.cloud.megumi.pattern;


import net.mamoe.mirai.message.data.MessageChain;

/**
 * @Description 匹配策略机
 * @Author alice
 * @Date 2020/12/25 15:47
 **/

public interface PatternStrategy {
    public Boolean isMatch(MessageChain messageChain, String botPrefix, String command, String... alias);
}
