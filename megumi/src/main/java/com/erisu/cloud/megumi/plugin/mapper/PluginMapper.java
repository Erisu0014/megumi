package com.erisu.cloud.megumi.plugin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.plugin.pojo.Plugin;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description pluginMapper
 * @Author alice
 * @Date 2021/1/8 14:12
 **/
@Mapper
public interface PluginMapper extends BaseMapper<Plugin> {
}
