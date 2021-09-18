package com.erisu.cloud.megumi.tekoki.pojo

data class Tekoki(var foods: MutableList<Food>) {
    data class Food(
        var name: String,
        var url: String,
        var pic: String,
    )
}
