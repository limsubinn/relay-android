package com.example.relay.timetable.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class GroupTimetableRes(
    @SerializedName("result") val result: GroupTimetableResult
) : BaseResponse()
