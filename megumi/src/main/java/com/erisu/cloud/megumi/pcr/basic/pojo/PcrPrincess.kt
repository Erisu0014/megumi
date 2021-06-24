package com.erisu.cloud.megumi.pcr.basic.pojo

data class PcrPrincess(
    val princessId: String,
    val name: String,
    val guild: String,//公会
    val birthday: String,
    val age: String,
    val height: String,
    val weight: String,
    val bloodType: String,
    val race: String,//种族
    val princessLike: String,
    val voice: String,
) {
    override fun toString(): String {
        return "${name}\n公会: ${guild}\n生日: ${birthday}\n年龄: ${age}\n身高: ${height}\n体重: ${weight}\n血型: ${bloodType}\n" +
                "种族: ${race}\n喜好: ${princessLike}\n声优: ${voice}\n"
    }
}
