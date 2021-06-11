package com.erisu.cloud.megumi.remind.pojo

data class RemindMe(
    val remindMeId:Int,
    var groupId:String?=null,
    var userId:String?=null,
    val value:String // mirai message
)
