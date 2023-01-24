package com.example.relay.login.data

import com.google.gson.annotations.SerializedName

data class SignInLocalReq(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("pwd") val pwd: String
)
