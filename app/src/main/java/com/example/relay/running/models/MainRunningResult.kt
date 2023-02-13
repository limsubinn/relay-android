package com.example.relay.running.models

import com.google.gson.annotations.SerializedName

data class MainRunningResult(
    @SerializedName("memberStatusIdx") val memberStatusIdx: Long,
    @SerializedName("userProfile") val userProfile: UserProfile,
    @SerializedName("timeTableRes") val timeTableRes: TimeTable,
    @SerializedName("runningRecord") val runningRecord: ArrayList<RunningRecord>,
)
