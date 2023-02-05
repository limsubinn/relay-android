package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class RecordWithoutLocation (
    @SerializedName("date") val date: String,
    @SerializedName("recordIdx") val recordIdx: Long,
    @SerializedName("runningStatus") val runningStatus: String
)