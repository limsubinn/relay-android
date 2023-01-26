package com.example.relay.login.data

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class BaseRes(
    @SerializedName("result") val result: BaseResult?
): BaseResponse()

data class BaseResult(
    val accessToken: String,
    val accessTokenExpiresIn: String,
    val grantType: String,
    val refreshToken: String
)
