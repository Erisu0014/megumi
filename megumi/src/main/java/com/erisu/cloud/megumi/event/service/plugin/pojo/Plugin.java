package com.erisu.cloud.megumi.event.service.plugin.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Description 插件entity
 * @Author alice
 * @Date 2021/1/8 14:12
 **/
@Data
public class Plugin {
    @TableId(value = "id")
    private String id;
    private String name;
    private String des;
}
