package com.example.relay.login.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserProfileRes(
    @SerializedName("result") val result: Long
) : BaseResponse()