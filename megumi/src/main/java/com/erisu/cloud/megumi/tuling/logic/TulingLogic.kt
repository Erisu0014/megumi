package com.erisu.cloud.megumi.tuling.logic

import cn.hutool.core.lang.UUID
import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.tuling.pojo.*
import com.erisu.cloud.megumi.util.FileUtil
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import kotlin.random.Random

/**
 *@Description tuling相关
 *@Author alice
 *@Date 2021/7/23 15:13
 **/
@Component
class TulingLogic : ApplicationRunner {
    companion object {
        private var words = mutableMapOf<String, MutableList<String>>()
    }
    @Value("\${tulingUrl}")
    public lateinit var tulingUrl: String

    @Value("\${apikey}")
    public lateinit var apikey: String

    fun onlineAnswering(text: String): Message? {
        val request =
            TulingRequest(0, Perception(Text(text), null, null, null), UserInfo(apikey, UUID.fastUUID().toString(true)))
        val response = HttpUtil.post(tulingUrl, JSON.toJSONString(request), 2000)
        val tulingResponse = JSONObject.parseObject(response, TulingResponse::class.java)
        return if (tulingResponse.results.isNotEmpty() && tulingResponse.results[0].resultType == "text") {
            val result = JSONObject.parseObject(tulingResponse.results[0].values.toString(), Text::class.java)
            PlainText(result.text)
        } else {
            null
        }
    }

    override fun run(args: ApplicationArguments?) {
        val path = "${FileUtil.localStaticPath}${File.separator}eroi${File.separator}data.json"
        val baseStr = File(path).readLines().joinToString(separator = "")
        JSONObject.parseObject(baseStr).forEach { t, u ->
            words[t] = u as MutableList<String>
        }
    }

    fun eroOnlineAnswering(text: String): Message? {
        if (words.containsKey(text)) {
            val answer = words[text]!![Random.nextInt(0, words[text]!!.size)]
            return PlainText(answer)
        } else {
            val wordFilter = words.keys.filter { w -> text.startsWith(w) }
            if (wordFilter.isNotEmpty()) {
                val word = wordFilter[Random.nextInt(0, wordFilter.size)]
                val answer = words[word]!![Random.nextInt(0, words[word]!!.size)]
                return PlainText(answer)
            } else {
                val finalWordsFilter = words.keys.filter { w -> text.contains(w) }
                if (finalWordsFilter.isNotEmpty()) {
                    val word = finalWordsFilter[Random.nextInt(0, finalWordsFilter.size)]
                    val answer = words[word]!![Random.nextInt(0, words[word]!!.size)]
                    return PlainText(answer)
                }
            }
        }
        return onlineAnswering(text)
    }


}