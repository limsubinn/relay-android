package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class MemberProfile (
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("statusMsg") val statusMsg: String,
    @SerializedName("userProfileIdx") val userIdx: Long
)