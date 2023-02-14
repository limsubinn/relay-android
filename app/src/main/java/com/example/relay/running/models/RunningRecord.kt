package com.example.relay.running.models

import com.google.gson.annotations.SerializedName

data class RunningRecord(
    @SerializedName("recordIdx") val recordIdx: Long,
    @SerializedName("date") val date: String,
    @SerializedName("runningStatus") val runningStatus: String,

)
