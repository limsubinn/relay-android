package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class UserInfoResult (
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String
)
