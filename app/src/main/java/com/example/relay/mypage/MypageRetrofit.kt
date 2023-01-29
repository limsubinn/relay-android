package com.example.relay.mypage

import com.example.relay.mypage.models.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MypageRetrofit {
    @GET("/users/profileList/{profileIdx}")
    fun getProfileRes(
        @Path("profileIdx") profileIdx: Long,
        @Query("name") name: String
    ) : Call<UserProfileResponse>
}