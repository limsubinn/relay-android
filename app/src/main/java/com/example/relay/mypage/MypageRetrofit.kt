package com.example.relay.mypage

import com.example.relay.mypage.models.UserClubResponse
import com.example.relay.mypage.models.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MypageRetrofit {
    @GET("/users/profileList")
    fun getProfileRes() : Call<UserProfileResponse>

    @GET("/users/clubs/accepted")
    fun getClubRes(
        @Query("id") userIdx: Long
    ) : Call<UserClubResponse>
}