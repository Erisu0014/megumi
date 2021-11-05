package com.erisu.cloud.megumi.remind.pojo

import lombok.Getter

/**
 * 提醒类型
 *
 * @constructor
 * 构造函数
 *
 * @param activity  活动名
 * @param remindBegin    0：不提醒；1：提醒  [活动开始前提醒]
 * @param remindLast     0：不提醒；1：提醒  [活动结束前提醒]
 */
@Getter
enum class RemindType(var activity: String, var remindBegin: Int, var remindLast: Int) {
    gvg("公会战", 1, 1), fes("剧情活动", 1, 1), gacha("卡池", 0, 1),
    playoff("季前赛", 1, 0), normal("normal", 1, 0), hard("hard", 1, 0),
    temple("神殿", 0, 0), princess_chip("心碎", 0, 0)
}