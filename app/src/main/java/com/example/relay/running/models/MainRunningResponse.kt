package com.example.relay.running.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class MainRunningResponse(
    @SerializedName("result") val result: ArrayList<MainRunningResult>
) : BaseResponse()
