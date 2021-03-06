package com.erisu.cloud.megumi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 数据源配置
 * @Author alice
 * @Date 2021/1/8 17:10
 **/
@Configuration
@MapperScan("com.erisu.cloud.megumi.**.mapper")
public class DataSourceConfig {
}
