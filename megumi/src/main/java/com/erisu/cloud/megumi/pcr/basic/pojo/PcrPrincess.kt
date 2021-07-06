package com.erisu.cloud.megumi.pcr.basic.pojo

data class PcrPrincess(
    @JvmField
    val princessId: String,
    @JvmField
    val name: String,
    @JvmField
    val guild: String,//公会
    @JvmField
    val birthday: String,
    @JvmField
    val age: String,
    @JvmField
    val height: String,
    @JvmField
    val weight: String,
    @JvmField
    val bloodType: String,
    @JvmField
    val race: String,//种族
    @JvmField
    val princessLike: String,
    @JvmField
    val voice: String,
    @JvmField
    val oppai: String,
) {
    override fun toString(): String {
        return "${name}\n公会: ${guild}\n生日: ${birthday}\n年龄: ${age}\n身高: ${height}\n体重: ${weight}\n血型: ${bloodType}\n" +
                "种族: ${race}\n喜好: ${princessLike}\n声优: ${voice}\nおっぱい：${oppai}"
    }
}
