package com.example.relay.mypage.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class ChangePwdResponse (
    @SerializedName("result") val result: String
) : BaseResponse()