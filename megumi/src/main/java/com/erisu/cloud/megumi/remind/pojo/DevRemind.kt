package com.erisu.cloud.megumi.remind.pojo

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

data class DevRemind(
    @TableId(type = IdType.AUTO)
    var id: Int? = null,
    var groupId: String? = null,
    var qqId: String? = null,
    val remindJson: String // mirai message
)
