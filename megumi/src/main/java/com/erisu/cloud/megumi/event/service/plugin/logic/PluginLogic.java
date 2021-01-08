package com.erisu.cloud.megumi.event.service.plugin.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erisu.cloud.megumi.event.service.plugin.mapper.GroupPluginMapper;
import com.erisu.cloud.megumi.event.service.plugin.mapper.PluginMapper;
import com.erisu.cloud.megumi.event.service.plugin.pojo.GroupPlugin;
import com.erisu.cloud.megumi.event.service.plugin.pojo.Plugin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 插件logic
 * @Author alice
 * @Date 2021/1/8 15:30
 **/
@Component
public class PluginLogic {
    @Resource
    private PluginMapper pluginMapper;

    @Resource
    private GroupPluginMapper groupPluginMapper;

    public List<Plugin> getPluginByName(String name) {
        QueryWrapper<Plugin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "name", name.trim());
        return pluginMapper.selectList(queryWrapper);
    }

    public List<GroupPlugin> getGroupPluginByName(String name) {
        return groupPluginMapper.getGroupPlugins(name);
    }


    public Boolean updateGroupPlugin(GroupPlugin groupPlugin) {
        QueryWrapper<GroupPlugin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "group_id", groupPlugin.getGroupId());
        queryWrapper.eq(true, "plugin_id", groupPlugin.getPluginId());

        if (groupPluginMapper.selectOne(queryWrapper) != null) {
            groupPluginMapper.updateById(groupPlugin);
        }
        return groupPluginMapper.insert(groupPlugin) > 0;
    }
}
