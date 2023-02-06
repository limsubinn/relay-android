package com.example.relay.group.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class GroupInfoResponse (
    @SerializedName("result") val result: GroupInfoResult
) : BaseResponse()
