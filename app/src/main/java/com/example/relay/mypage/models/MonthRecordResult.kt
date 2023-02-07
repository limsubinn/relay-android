package com.example.relay.mypage.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class MonthRecordResult (
    @SerializedName("avgPace") val avgPace: Double,
    @SerializedName("date") val date: String,
    @SerializedName("totalDist") val totalDist: Double,
    @SerializedName("totalTime") val totalTime: Double
)