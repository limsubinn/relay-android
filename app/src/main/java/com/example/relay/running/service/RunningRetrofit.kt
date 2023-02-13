package com.example.relay.running.service

import com.example.relay.running.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RunningRetrofit {
    @POST("/record/start")
    fun postRunStrRes(
        @Body runningInitReq: RunStrRequest
    ) : Call<RunStrResponse>

    @POST("/record/finish")
    fun postRunEndRes(
        @Body runningFinishReq: RunEndRequest
    ) : Call<RunEndResponse>

    @GET("/clubs/home/{userProfileIdx}")
    fun getRunMainRes(
        @Path("userProfileIdx") userProfileIdx: Long
    ) : Call<MainRunningResponse>
}