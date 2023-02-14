package com.example.relay.running.models

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("userProfileIdx") val userProfileIdx: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("statusMsg") val statusMsg: String,
    @SerializedName("imgUrl") val imgUrl: String,

)
