package com.erisu.cloud.megumi.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 类型枚举类（还是工厂模式好了，总之可以先这样）
 */
@Getter
@AllArgsConstructor
public enum Pattern {
    CONTAINS(1,"包含"),PREFIX(2,"前缀"),SUFFIX(3,"后缀"),EQUALS(4,"等于");
    private Integer type;
    private String typeName;
}
