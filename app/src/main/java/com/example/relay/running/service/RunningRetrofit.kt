package com.example.relay.running.service

import com.example.relay.BaseResponse
import com.example.relay.running.models.RunStrRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RunningRetrofit {
    @POST("/record/start")
    fun postRunStrRes(
        @Body runningInitReq: RunStrRequest
    ) : Call<BaseResponse>
}