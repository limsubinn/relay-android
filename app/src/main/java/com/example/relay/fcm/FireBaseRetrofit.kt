package com.example.relay.fcm

import com.example.relay.BaseResponse
import com.example.relay.fcm.data.UserDeviceTokenReq
import com.example.relay.fcm.data.UserDeviceTokenRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FireBaseRetrofit {
    @POST("users/device/")
    fun postDeviceReq(
        @Body req : UserDeviceTokenReq
    ): Call<UserDeviceTokenRes>

    @POST("users/device/delete")
    fun deleteDeviceReq(
        @Body req : UserDeviceTokenReq
    ) : Call<UserDeviceTokenRes>
}