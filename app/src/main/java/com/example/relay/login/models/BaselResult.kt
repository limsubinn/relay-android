package com.example.relay.login.models

import com.google.gson.annotations.SerializedName

data class BaselResult(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("accessTokenExpiresIn") val accessTokenExpiresIn: String,
    @SerializedName("grantType") val grantType: String,
    @SerializedName("refreshToken") val refreshToken: String
)
