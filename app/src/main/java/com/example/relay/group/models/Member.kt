package com.example.relay.group.models

import com.example.relay.timetable.models.MemSchedule
import com.google.gson.annotations.SerializedName

data class Member (
    @SerializedName("memberStatusIdx") val statusIdx: Long,
    @SerializedName("runningRecord") val record: ArrayList<RecordWithoutLocation>,
    @SerializedName("timeTableRes") val timeTableRes: MemSchedule,
    @SerializedName("userProfile") val profile: MemberProfile
)