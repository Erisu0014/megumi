package com.erisu.cloud.megumi.tuling.pojo

import com.alibaba.fastjson.annotation.JSONField
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
    @JSONField(name="refresh_token")
    val refresh_token: String?=null,
    @JSONField(name="expires_in")
    val expires_in: Int?=null,
    @JSONField(name="session_key")
    val session_key: String?=null,
    @JSONField(name="access_token")
    val access_token: String?=null,
    @JSONField(name="scope")
    val scope: String?=null,
    @JSONField(name="session_secret")
    val session_secret: String?=null,
)
