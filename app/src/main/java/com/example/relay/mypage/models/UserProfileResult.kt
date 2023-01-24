package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class UserProfileResult (
    @SerializedName("userProfileIdx") val userIdx: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("statusMsg") val statusMsg: String,
    @SerializedName("isAlarmOn") val isAlarmOn: String,
    @SerializedName("imgUrl") val imgUrl: String
)
