package com.erisu.cloud.megumi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum RedisKey {
    PLUGIN("plugin");
    private String name;

    RedisKey(String name) {
        this.name = name;
    }
}
