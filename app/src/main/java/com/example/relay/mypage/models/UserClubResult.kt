package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class UserClubResult (
    @SerializedName("clubIdx")  val clubIdx: Long,
    @SerializedName("name") val name: String
)
