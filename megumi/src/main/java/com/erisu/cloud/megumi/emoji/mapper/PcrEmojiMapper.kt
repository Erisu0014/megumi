package com.erisu.cloud.megumi.emoji.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.erisu.cloud.megumi.emoji.pojo.PcrEmoji

/**
 *@Description pcr表情相关
 *@Author alice
 *@Date 2021/6/25 16:36
 **/
interface PcrEmojiMapper : BaseMapper<PcrEmoji> {
    fun selectRandom(): PcrEmoji
}