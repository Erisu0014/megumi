package com.erisu.cloud.megumi.lol.logic

import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.lol.pojo.LplBasicInfo
import com.erisu.cloud.megumi.util.FileUtil
import org.springframework.stereotype.Component
import java.io.File

@Component
class LplSpringInitData {
    var isInited = false
    val daySpringData: MutableMap<String, MutableList<LplBasicInfo>> = mutableMapOf()
    val teamSpringData: MutableMap<String, MutableList<LplBasicInfo>> = mutableMapOf()

    @Synchronized
    fun initData() {
        if (!isInited) {
            val dataFile = File("${FileUtil.localStaticPath}${File.separator}lol${File.separator}世界赛.json")
            val springData = dataFile.readLines().joinToString(separator = "") { it }
            val lplBasicInfoList: List<LplBasicInfo> = JSONObject.parseArray(springData, LplBasicInfo::class.java)
            lplBasicInfoList.forEach {
                val splits = it.time.split(" ")
                if (!daySpringData.containsKey(splits[0])) {
                    daySpringData[splits[0]] = mutableListOf()
                }
                daySpringData[splits[0]]!!.add(it)
                if (!teamSpringData.containsKey(it.b)) {
                    teamSpringData[it.b] = mutableListOf()
                }
                teamSpringData[it.b]!!.add(it)
                if (!teamSpringData.containsKey(it.r)) {
                    teamSpringData[it.r] = mutableListOf()
                }
                teamSpringData[it.r]!!.add(it)
            }
            isInited = true
        }
    }
}