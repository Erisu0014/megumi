package com.erisu.cloud.megumi.command

/**
 *@Description
 *@Author alice
 *@Date 2021/6/23 18:16
 **/
enum class CommandType(val type: Int, val typeName: String) {
    FRIEND(0, "友達"), GROUP(1, "群聊");
}