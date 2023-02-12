package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class ChangeMsgRequest(
    @SerializedName("statusMsg") val statusMsg: String
)
