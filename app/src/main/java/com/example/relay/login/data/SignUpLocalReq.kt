package com.example.relay.login.data

import com.google.gson.annotations.SerializedName

data class SignUpLocalReq(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("pwd") val pwd: String
)
