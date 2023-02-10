package com.example.relay.mypage.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class MonthRecordResponse (
    @SerializedName("result") val result: ArrayList<MonthRecordResult>
) : BaseResponse()