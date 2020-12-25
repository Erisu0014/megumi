package com.erisu.cloud.megumi.command;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 就两个不用工厂-策略模式吧，再说
 */
@Getter
@AllArgsConstructor
public enum CommandType {
    FRIEND(0,"友達"),GROUP(1,"群聊");
    private Integer type;
    private String typeName;

}
