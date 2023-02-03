package com.example.relay.timetable

import com.example.relay.BaseResponse
import com.example.relay.timetable.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TimetableRetrofit {
    @GET("/clubs/member-status/{clubIdx}/time-tables")
    fun getGroupTimetablesReq(
        @Path("clubIdx") clubIdx:Long
    ) : Call<GroupTimetableRes>

    @POST("clubs/member-status/{clubIdx}")
    fun postMyTimetableReq(
        @Path("clubIdx") clubIdx:Long,
        @Body timeTables: MySchedulesReq
    ) : Call<BaseResponse>
}