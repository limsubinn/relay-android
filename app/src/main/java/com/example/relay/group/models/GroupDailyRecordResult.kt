package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class GroupDailyRecordResult (
    @SerializedName("avgPace") val avgPace: Float,
    @SerializedName("date") val date: String,
    @SerializedName("goalType") val goalType: String,
    @SerializedName("goalValue") val goalValue: Float,
    @SerializedName("totalDist") val totalDist: Float,
    @SerializedName("totalTime") val totalTime: Float
)