package com.example.relay.timetable.models

import com.google.gson.annotations.SerializedName

data class GroupTimetableList(
    @SerializedName("userProfileIdx") val profileIdx:Long,
    val nickName:String,
    val timeTables: ArrayList<GroupMemSchedule>
)
