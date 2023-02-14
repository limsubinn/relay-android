package com.example.relay.running.models

import com.google.gson.annotations.SerializedName

data class TimeTable(
    @SerializedName("timeTableIdx") val timeTableIdx: Long,
    @SerializedName("day") val day: Long,
    @SerializedName("start") val start: String,
    @SerializedName("end") val end: String,
    @SerializedName("goal") val goal: Long,
    @SerializedName("goalType") val goalType: String,
)
