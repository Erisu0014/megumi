package com.erisu.cloud.megumi.plugin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.plugin.pojo.GroupPlugin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description groupPluginMapper
 * @Author alice
 * @Date 2021/1/8 14:12
 **/

public interface GroupPluginMapper extends BaseMapper<GroupPlugin> {

    List<GroupPlugin> getGroupPlugins(@Param("name") String name, @Param("id") String groupId);

    int updateGroupPlugin(@Param("groupPlugin") GroupPlugin groupPlugin);
}
