package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class UserProfileResult (
    @SerializedName("clubIdx") val clubIdx: Long,
    @SerializedName("clubName") val clubName: String,
    @SerializedName("email") val email: String,
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("isAlarmOn") val isAlarmOn: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("statusMsg") val statusMsg: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userProfileIdx") val userIdx: Long
)
