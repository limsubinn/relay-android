package com.example.relay.login.data

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class LogInLocalRes(
    @SerializedName("result") val result: BaselResult
) : BaseResponse()
