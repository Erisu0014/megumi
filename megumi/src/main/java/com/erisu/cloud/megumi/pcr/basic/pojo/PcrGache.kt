package com.erisu.cloud.megumi.pcr.basic.pojo

import com.alibaba.fastjson.annotation.JSONField

data class PcrGacha(
    @JSONField(name = "JP")
    val JP: SingleGacha,

    @JSONField(name = "BL")
    val BL: SingleGacha,

    @JSONField(name = "TW")
    val TW: SingleGacha,

    @JSONField(name = "ALL")
    val ALL: SingleGacha,
) {
    data class SingleGacha(
        @JSONField(name = "up_prob")
        val up_prob: Int,

        @JSONField(name = "s3_prob")
        val s3_prob: Int,

        @JSONField(name = "s2_prob")
        val s2_prob: Int,

        @JSONField(name = "up")
        val up: MutableList<Int>,

        @JSONField(name = "star3")
        val star3: MutableList<Int>,

        @JSONField(name = "star2")
        val star2: MutableList<Int>,

        @JSONField(name = "star1")
        val star1: MutableList<Int>,
    )

}
