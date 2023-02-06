package com.example.relay.timetable.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class MyTimetableRes (
    @SerializedName("result") val result: ArrayList<MemSchedule>
) : BaseResponse()
