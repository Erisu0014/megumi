package com.erisu.cloud.megumi.osu.pojo

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.erisu.cloud.megumi.osu.pojo.BgUtil.nameCheck

object BgUtil {
    const val bgList = "{\n" +
            "  \"2586617\": [\"Nakayoshi Sensation\",\"好朋友部\",\"なかよし部\",\"なかよし\",\"プリコネ\",\"プリコネ\",\"papapa213\"],\n" +
            "  \"1722410\": [\"God-ish\",\"god-ish\",\"神っぽい\",\"神っぽいな\",\"DECO*27\",\"deco*27\"],\n" +
            "  \"1055780\": [\"Midorigo Queen Bee\",\"うみねこのなく頃に\",\"嬰児インビー\",\"luscent\",\"umineko\"],\n" +
            "  \"963814\": [\"Hachigatsu, Bou, Tsukiakari\",\"八月某\",\"八月、某、月明かり\",\"yorushika\",\"ヨルシカ\",\"delis\"],\n" +
            "  \"686777\": [\"paradise\",\"sound souler\",\"kwk\",\"arcaea\"],\n" +
            "  \"596704\": [\"hitorigoto\",\"Hitorigoto -TV MIX-\",\"claris\",\"エロマンガ先生\",\"doormat\"],\n" +
            "  \"372510\": [\"Kyouran Hey Kids!!\",\"Kyouran Hey Kids\",\"狂乱 Hey Kids\",\"狂乱 Hey Kids!!\",\"monstrata\",\"ノラガミ ARAGOTO\",\"THE ORAL CIGARETTES\"],\n" +
            "  \"219380\": [\"Toumei Elegy\",\"偷煤哀歌\",\"konuko\",\"awaken\",\"透明エレジー\"],\n" +
            "  \"842412\": [\"oneRoom\",\"Sotarks\",\"花坂結衣\",\"MAO\",\"Harumachi Clover\",\"春待ちコローバー\"],\n" +
            "  \"41686\": [\"Scarlet Rose\",\"血玫瑰\",\"val0108\",\"seleP\"],\n" +
            "  \"580215\": [\"dorchadas\",\"Rita\",\"Delis\",\"漆黒のシャルノス\"],\n" +
            "  \"444335\": [\"kirakiraDays\",\"Kira Kira Days\",\"k-on\",\"けいおん\",\"闪闪日\"],\n" +
            "  \"352570\": [\"Night of Knights\",\"夜骑士\",\"beatMARIO\",\"alacat\",\"十六夜咲夜\",\"東方花映塚\"],\n" +
            "  \"361740\": [\"Get Jinxed\",\"Agnete Kjolsrud\",\"金克斯\",\"金克丝\",\"Tarrasky\"],\n" +
            "  \"653534\": [\"ILY\",\"Panda Eyes\",\"M a r v o l l o\"],\n" +
            "  \"384772\": [\"Asu no Yozora Shoukaihan\",\"アスノヨゾラ哨戒班\",\"哨戒班\",\"yuaru\",\"Akitoshi\"],\n" +
            "  \"292301\": [\"Blue Zenith\",\"蓝极光\",\"蓝激光\",\"xi\",\"Asphyxia\",\"727\",\"blueZenith\"],\n" +
            "  \"28705\": [\"Kokou no Sousei\",\"Yousei Teikoku\",\"妖精帝国\",\"孤高\",\"孤高的创世\"],\n" +
            "  \"478405\": [\"Snow Drive(01.23)\",\"Snow Drive\",\"雪司机\",\"omoi\",\"kroytz\"]\n" +
            "}"
     val bgJson = JSONObject.parseObject(bgList)

    fun nameCheck(name: String, id: String): Boolean {
        if (bgJson[id] != null) {
            val tempJson = bgJson[id] as JSONArray
            val result = tempJson.firstOrNull { it.toString().uppercase() == name.uppercase() }
            return result != null
        }
        return false
    }
}

