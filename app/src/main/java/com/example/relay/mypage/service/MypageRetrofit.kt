package com.example.relay.mypage.service

import com.example.relay.BaseResponse
import com.example.relay.mypage.models.*
import retrofit2.Call
import retrofit2.http.*

interface MypageRetrofit {
    @GET("/users/profileList/{profileIdx}")
    fun getProfileRes(
        @Path("profileIdx") profileIdx: Long
    ) : Call<UserProfileResponse>

    @PATCH("/users/pwd")
    fun patchUserPwdReq(
        @Body userPwd: ChangePwdRequest
    ) : Call<BaseResponse>

    @PATCH("/users/changeProfile")
    fun patchUserMsgReq(
        @Body profileReq: ChangeMsgRequest
    ) : Call<BaseResponse>

    @PATCH("/users/changeProfile")
    fun patchUserImgReq(
        @Body profileReq: ChangeImgRequest
    ) : Call<BaseResponse>

    @PATCH("/users/changeAlarm/{profileIdx}")
    fun patchUserAlarm(
        @Path("profileIdx") profileIdx: Long
    ) : Call<BaseResponse>

    @GET("/record")
    fun getDailyRes(
        @Query("date") date: String,
        @Query("idx") idx: Long
    ) : Call<DailyRecordResponse>

    @GET("/record/calender")
    fun getMonthRes(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("profileIdx") profileIdx: Long
    ) : Call<MonthRecordResponse>
}