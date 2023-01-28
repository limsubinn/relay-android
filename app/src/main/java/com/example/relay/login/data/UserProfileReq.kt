package com.example.relay.login.data

import com.google.gson.annotations.SerializedName

data class UserProfileReq(
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("isAlarmOn") val isAlarmOn: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("statusMsg") val statusMsg: String
)
