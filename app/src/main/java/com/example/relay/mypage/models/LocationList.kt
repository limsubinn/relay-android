package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class LocationList(
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("status") val status: String,
    @SerializedName("time") val time: String
)
