package com.example.relay.ui

import com.example.relay.ui.models.UserInfoResponse
import com.example.relay.ui.models.UserProfileListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MainRetrofit {
    @GET("/users/")
    fun getUserRes() : Call<UserInfoResponse>

    @GET("/users/profileList")
    fun getProfileListRes(
        @Query("name") name: String
    ) : Call<UserProfileListResponse>

}