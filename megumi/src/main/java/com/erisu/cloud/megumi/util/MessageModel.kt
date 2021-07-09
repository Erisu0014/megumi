package com.erisu.cloud.megumi.util

/**
 *@Description TODO
 *@Author alice
 *@Date 2021/7/8 18:16
 **/
enum class MessageModel (val type:Int){
    DEFAULT(0),PLAIN_TEXT(1),FORWARD_MESSAGE(2),IMAGE(3)
}