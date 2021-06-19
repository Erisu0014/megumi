package com.erisu.cloud.megumi.util

import java.nio.charset.Charset


/**
 *@Description 切噜语转换
 *@Author alice
 *@Date 2021/6/15 17:17
 **/
object CheRuEncoder {
    private var charsetDict: MutableMap<String, Int> = mutableMapOf()
    private var charsetEncoding: String = "切卟叮咧哔唎啪啰啵嘭噜噼巴拉蹦铃"

    init {
        charsetEncoding.forEachIndexed { index, c -> charsetDict[c.toString()] = index }
    }

    fun word2cheru(w: String): String {
        var res = "切"
        //对于base16而言无须补位
        val toByteArray = w.toByteArray(Charset.forName("GB2312"))
        toByteArray.forEach {
            res = res + charsetEncoding[it.toInt() and 0xf] + charsetEncoding[it.toInt().shr(4) and 0xf]
        }
        return res
    }

    fun cheru2word(w: String): String {
        if (w[0] != '切' || w.length < 2) return w
        var result = byteArrayOf()
        for (i in 1 until w.length step 2) {
            val x = charsetDict[w[i + 1].toString()]?.shl(4)!! or charsetDict[w[i].toString()]!!
            result += x.toByte()
        }
        return String(result, Charset.forName("GB2312"))

    }
}
