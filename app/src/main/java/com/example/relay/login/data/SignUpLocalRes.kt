package com.example.relay.login.data

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class SignUpLocalRes(
    @SerializedName("result") val result: BaselResult
) : BaseResponse()
