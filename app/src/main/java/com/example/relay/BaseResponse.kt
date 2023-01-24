package com.example.relay

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("code") val code: Int = 0,
    @SerializedName("isSuccess") val isSuccess: Boolean = false,
    @SerializedName("message") val message: String? = null
)

