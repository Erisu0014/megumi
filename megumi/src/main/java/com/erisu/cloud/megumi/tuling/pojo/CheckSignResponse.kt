package com.erisu.cloud.megumi.tuling.pojo

import kotlinx.serialization.Serializable

/**
 * 鉴权response
 *
 * @property refresh_token
 * @property expires_in
 * @property session_key
 * @property access_token
 * @property scope
 * @property session_secret
 */
@Serializable
data class CheckSignResponse(
    val refresh_token: String,
    val expires_in: Int,
    val session_key: String,
    val access_token: String,
    val scope: String,
    val session_secret: String,
)
