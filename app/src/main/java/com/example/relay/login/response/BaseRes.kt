package com.example.relay.login.response

import com.google.gson.annotations.SerializedName

data class BaseRes(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BaseResult?
)

data class BaseResult(
    val accessToken: String,
    val accessTokenExpiresIn: String,
    val grantType: String,
    val refreshToken: String
)
