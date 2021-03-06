package com.erisu.cloud.megumi.analysis.annotation;

import com.erisu.cloud.megumi.command.CommandType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAnalysis {
    CommandType name();
}
