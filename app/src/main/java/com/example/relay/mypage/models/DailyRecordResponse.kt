package com.example.relay.mypage.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class DailyRecordResponse (
    @SerializedName("result") val result: DailyRecordResult
) : BaseResponse()