package com.erisu.cloud.megumi.pattern;

import cn.hutool.core.util.EnumUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 条件工厂
 * @Author alice
 * @Date 2020/12/25 15:45
 **/
@Component
public class PatternFactory implements ApplicationContextAware {

    private static volatile Map<Pattern, PatternStrategy> patternMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PatternStrategy> patternBeans = applicationContext.getBeansOfType(PatternStrategy.class);

        patternBeans.forEach((k, v) -> {
            PatternSupport annotation = v.getClass().getAnnotation(PatternSupport.class);
            if (null != annotation) {
                patternMap.put(annotation.pattern(), v);
            }
        });
    }

    public static PatternStrategy getProcessorHandler(Pattern pattern) {
        return patternMap.get(pattern);
    }
}
