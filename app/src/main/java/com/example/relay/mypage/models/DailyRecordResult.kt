package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class DailyRecordResult (
    @SerializedName("avgPace") val avgPace: Float,
    @SerializedName("date") val date: String,
    @SerializedName("totalDist") val totalDist: Float
)
