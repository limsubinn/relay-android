package com.example.relay.mypage.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserProfileResponse (
    @SerializedName("result") val result: UserProfileResult
) : BaseResponse()