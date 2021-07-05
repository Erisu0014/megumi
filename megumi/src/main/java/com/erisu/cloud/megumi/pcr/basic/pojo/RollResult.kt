package com.erisu.cloud.megumi.pcr.basic.pojo

data class RollResult(
    var result: MutableList<Int> = mutableListOf(),
    var s3_num: Int = 0,
    var s2_num: Int = 0,
    var s1_num: Int = 0,
    var reward: Int = 0,
    var firstUp: Int = 201,
    var memoryChip: Int = 0,
)
