package com.example.relay.fcm.data

import com.google.gson.annotations.SerializedName

data class UserDeviceTokenReq(
    @SerializedName("userDeviceID") val token:String
)
