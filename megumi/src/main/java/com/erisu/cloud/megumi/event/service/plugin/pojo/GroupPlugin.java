package com.erisu.cloud.megumi.event.service.plugin.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 关系
 * @Author alice
 * @Date 2021/1/8 15:36
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPlugin {
    @TableField("group_id")
    private String groupId;
    @TableField("plugin_id")
    private String pluginId;
    @TableField("enabled")
    private Integer enabled;
}
