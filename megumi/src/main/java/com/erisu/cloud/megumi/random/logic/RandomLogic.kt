package com.erisu.cloud.megumi.random.logic

import cn.hutool.core.util.NumberUtil
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import kotlin.random.Random

object RandomLogic {
    fun roll(message: MessageChain): Message {
        var end: Number = 0
        var begin: Number = 0
        var question = ""
        val randomNum: Number?
        val textMsg = message[1]    //prefix校验过，一定为plainText
        if (textMsg.toString() == "/roll") {
            randomNum = Random.nextDouble()
            val randomDecimal = String.format("%.2f", randomNum)
            return PlainText("概率值为:$randomDecimal")
        }
        // 此处不需要做正则匹配。直接拿整句分割就行
//        val pattern = Regex("""(\d+(\.\d+)?)( )?(\d+(\.\d+)?)?( )?(\w+)?""")
//        val rollStr = pattern.find(textMsg.toString())?.value ?: throw NullPointerException()
        val rollStr = textMsg.toString().removePrefix("/roll").trim()
        val splitValue = Regex("""( )+""").split(rollStr, 3)
        if (splitValue.isEmpty()) throw TypeCastException("不匹配/roll规则")
        if (!NumberUtil.isNumber(splitValue[0]) && splitValue.size == 1) {
            question = splitValue[0]
            randomNum = Random.nextDouble()
            val randomDecimal = String.format("%.2f", randomNum)
//            return PlainText("概率值为:$randomDecimal,问题：$question")
            return PlainText(randomDecimal)
        }
        when (splitValue.size) {
            1 -> end = splitValue[0].toDouble()
            2 -> when {
                NumberUtil.isNumber(splitValue[1]) -> {
                    begin = splitValue[0].toDouble()
                    end = splitValue[1].toDouble()
                }
                else -> {
                    end = splitValue[0].toDouble()
                    question = splitValue[1]
                }
            }
            3 -> when {
                NumberUtil.isNumber(splitValue[1]) -> {
                    begin = splitValue[0].toDouble()
                    end = splitValue[1].toDouble()
                    question = splitValue[2]
                }
                else -> {
                    end = splitValue[0].toDouble()
                    question = splitValue[1] + " " + splitValue[2]
                }
            }
        }
        return if (begin.toDouble().compareTo(begin.toInt()) == 0 && end.toDouble().compareTo(end.toInt()) == 0) {
            randomNum = Random.nextInt(begin.toInt(), end.toInt())
            PlainText(randomNum.toString())
        } else {
            randomNum = Random.nextDouble(begin.toDouble(), end.toDouble())
            val randomDecimal = String.format("%.2f", randomNum)
            PlainText(randomDecimal)
        }
//        return if (question.isNotBlank()) PlainText("概率为:$randomDecimal,问题：$question") else PlainText("概率值为:$randomDecimal")

    }
}

fun main() {
}