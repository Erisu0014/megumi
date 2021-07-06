package com.erisu.cloud.megumi.pcr.basic.util

/**
 *@Description 角色属性
 *@Author alice
 *@Date 2021/7/6 9:43
 **/
enum class CharacterField(var key: String, var field: String) {
    NAME("名字", "name"),
    GUILD("公会", "guild"),//公会
    BIRTHDAY("生日", "birthday"),
    AGE("年龄", "age"),
    HEIGHT("身高", "height"),
    WEIGHT("体重", "weight"),
    BLOOD_TYPE("血型", "bloodType"),
    RACE("种族", "race"),//种族
    PRINCESS_LIKE("喜好", "princessLike"),
    VOICE("声优", "voice"),
    OPPAI("罩杯", "oppai")
}