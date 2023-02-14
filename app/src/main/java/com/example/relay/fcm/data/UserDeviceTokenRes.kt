package com.example.relay.fcm.data

import com.example.relay.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserDeviceTokenRes(
    @SerializedName("status") val status: String
):BaseResponse()
