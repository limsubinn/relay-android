package com.example.relay.running.models

import com.google.gson.annotations.SerializedName

data class RunStrResult(
    @SerializedName("end") val end: String,
    @SerializedName("goal") val goal: Float,
    @SerializedName("goalType") val goalType: String,
    @SerializedName("runningRecordIdx") val runningRecordIdx: Long,
    @SerializedName("start") val start: String,
    )
