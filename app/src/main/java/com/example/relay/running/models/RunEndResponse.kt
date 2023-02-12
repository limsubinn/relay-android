package com.example.relay.running.models

import com.example.relay.BaseResponse
import com.example.relay.running.service.RunEndResult
import com.google.gson.annotations.SerializedName

data class RunEndResponse(
    @SerializedName("result") val result: RunEndResult
) : BaseResponse()
