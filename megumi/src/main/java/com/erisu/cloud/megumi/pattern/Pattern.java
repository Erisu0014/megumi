package com.erisu.cloud.megumi.pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 类型枚举类（还是工厂模式好了，总之可以先这样）
 */
@Getter
@AllArgsConstructor
public enum Pattern {
    CONTAINS(1, "包含"), PREFIX(2, "前缀"), SUFFIX(3, "后缀"), EQUALS(4, "等于"),
    MD5(5, "md5比较"), CHECK(6, "检测文本属性"), REGEX(7, "正则表达式"),CHECK_IMAGE(8, "检测图片"),
    CHECK_AT(9, "at"),;
    private final Integer type;
    private final String typeName;
}
