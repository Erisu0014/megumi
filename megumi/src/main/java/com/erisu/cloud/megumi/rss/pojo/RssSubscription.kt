package com.erisu.cloud.megumi.rss.pojo

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

data class RssSubscription(
    @TableId(type = IdType.AUTO)
    var id: Int?,
    var groupId: String,
    var rssUrl: String,
    var nickname:String?,
    val type:String
)
