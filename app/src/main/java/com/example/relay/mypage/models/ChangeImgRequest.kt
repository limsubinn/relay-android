package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class ChangeImgRequest(
    @SerializedName("imgUrl") val imgUrl: String
)
