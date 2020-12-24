package com.erisu.cloud.megumi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Module {
    PRINCESS("princess", true), DEFAULT("default", true);
    private String name;
    private boolean autowired = true;

}
