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

    @GET("/clubs/member-status/time-tables/{userProfileIdx}")
    fun getMyTimetableReq(
        @Path("userProfileIdx") userProfileIdx:Long
    ) : Call<MyTimetableRes>

    @POST("/clubs/member-status/{clubIdx}")
    fun postMyTimetableReq(
        @Path("clubIdx") clubIdx:Long,
        @Body timeTables: MySchedulesReq
    ) : Call<BaseResponse>
}