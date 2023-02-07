package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class ChangePwdRequest(
    @SerializedName("newPwd") val newPwd: String,
    @SerializedName("newPwdCheck") val newPwdCheck: String
)
