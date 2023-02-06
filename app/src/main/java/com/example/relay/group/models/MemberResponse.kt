package com.example.relay.group.models

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class MemberResponse (
    @SerializedName("result") val result: ArrayList<Member>
) : BaseResponse()
