package com.example.relay.ui.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserInfoResponse (
    @SerializedName("result") val result: UserInfoResult
) : BaseResponse()