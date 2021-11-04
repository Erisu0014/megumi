package com.erisu.cloud.megumi.tuling.pojo

import kotlinx.serialization.Serializable

/**
 * 百度鉴权异常处理
 *
 * @property error
 * @property error_description
 */
@Serializable
data class CheckSignErrorResponse(val error:String,val error_description:String)
