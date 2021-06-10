package com.erisu.cloud.megumi.exception

import net.mamoe.mirai.contact.Contact


data class MegumiException(
    val returnMsg: String,//用以传递给用户的会话
    val subject: Contact
) : Exception()
