package com.example.relay.group.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class GroupListResponse (
    @SerializedName("result") val result: ArrayList<GroupListResult>
) : BaseResponse()
