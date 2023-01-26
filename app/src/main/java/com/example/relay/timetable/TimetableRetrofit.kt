package com.example.relay.timetable

import com.example.relay.timetable.models.GroupTimetableRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TimetableRetrofit {
    @GET("clubs/{idx}/timetables")
    fun getGroupTimetables(
        @Path("clubIdx") clubIdx:Int
    ) : Call<GroupTimetableRes>

    @POST("clubs/{idx}/timetables/{idx}")
    fun postMyTimetable(
        @Path("clubIdx") clubIdx:Int,
        @Path("timetableIdx") timetableIdx:Int
    )
}