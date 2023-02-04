package com.example.relay.mypage

import com.example.relay.login.data.LogInLocalReq
import com.example.relay.mypage.models.*
import com.example.relay.ui.models.UserProfileListResponse
import retrofit2.Call
import retrofit2.http.*

interface MypageRetrofit {
    @GET("/users/profileList/{profileIdx}")
    fun getProfileRes(
        @Path("profileIdx") profileIdx: Long,
        @Query("name") name: String
    ) : Call<UserProfileResponse>

    @GET("/record/daily")
    fun getDailyRes(
        @Query("date") date: String
    ) : Call<DailyRecordResponse>

    @PATCH("/users/pwd")
    fun patchUserPwdReq(
        @Body userPwd: ChangePwdRequest
    ) : Call<ChangePwdResponse>

    @PATCH("/users/changeProfile")
    fun patchUserMsgReq(
        @Body profileReq: String
    ) : Call<ChangeMsgResponse>
}