package com.erisu.cloud.megumi.jap.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Description 日语词汇
 * @Author alice
 * @Date 2021/1/9 16:07
 **/
@Data
public class JapWords {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String word;
    private String pseudonym;
    private String chinese;
    private String type;
    @TableField("insertTag")
    private Integer insertTag; // 0 表示从文件读取的初始化脚本，1表示用户自己添加的
}
