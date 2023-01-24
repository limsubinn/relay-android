package com.example.relay.mypage.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserClubResponse (
    @SerializedName("result") val result: UserClubResult
) : BaseResponse()