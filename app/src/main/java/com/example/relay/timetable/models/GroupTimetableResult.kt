package com.example.relay.timetable.models

import com.google.gson.annotations.SerializedName

data class GroupTimetableResult(
    @SerializedName("clubIdx")  val clubIdx: Long,
    @SerializedName("name") val name: String
)
