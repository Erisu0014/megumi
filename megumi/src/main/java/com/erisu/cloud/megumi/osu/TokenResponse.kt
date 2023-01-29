package com.erisu.cloud.megumi.osu

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken:String,
    val expires_in:Int,
    val refresh_token:String,
    val token_type:String,

)
