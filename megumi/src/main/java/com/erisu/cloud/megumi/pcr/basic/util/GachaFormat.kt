package com.erisu.cloud.megumi.pcr.basic.util

import com.erisu.cloud.megumi.pcr.basic.pojo.RollResult
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf

object GachaFormat {

    /**
     * 您是🐕吧
     */
    fun dog(rollResult: RollResult, image: Image): Message {
        return messageChainOf(
            PlainText("素敵な仲間が増えますよ！ \n"),
            image,
            PlainText(
                "★★★×${rollResult.s3_num} ★★×${rollResult.s2_num} ★×${rollResult.s1_num}\n" +
                        "获得记忆碎片${rollResult.memoryChip}与女神秘石×${rollResult.reward}！\n" +
                        "第${rollResult.firstUp}抽首次获得up角色\n" +
                        "记忆碎片一大堆！您是🐕吧？\n"
            )
        )
    }

    /**
     * 您是🐱吧
     */
    fun cat(rollResult: RollResult, image: Image): Message {
        return messageChainOf(
            PlainText("素敵な仲間が増えませんよ~ \n"),
            image,
            PlainText(
                "★★★×${rollResult.s3_num} ★★×${rollResult.s2_num} ★×${rollResult.s1_num}\n" +
                        "获得女神秘石×${rollResult.reward}！\n" +
                        "热知识：天井的概率有24.114514%\n"
            )
        )
    }
}