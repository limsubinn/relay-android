package com.example.relay.login.service

import com.example.relay.login.models.*
import retrofit2.Call
import retrofit2.http.*

interface LogInRetrofit {
    @POST("users/logIn")
    fun postLogInLocalReq(
        @Body Info: LogInLocalReq
    ): Call<LogInLocalRes>

    @POST("users/sign-in")
    fun postSignUpLocalReq(
        @Body Info: SignUpLocalReq
    ): Call<SignUpLocalRes>

    @POST("users/logIn/Google")
    fun logInGoogle(): Call<BaseRes>

    @POST("users/logIn/Naver")
    fun logInNaver(): Call<BaseRes>

    @POST("users/logIn/Kakao")
    fun logInKakao(): Call<BaseRes>

    @PATCH("users/pwd")
    fun findPw(): Call<BaseRes>

    @POST("users/profile") // 프로필 신규 생성
    fun postUserProfileReq(
        @Query("name") name: String,
        @Body Info: UserProfileReq
    ): Call<UserProfileRes>

    @POST("users/email")
    fun sendConfirmCode(
        @Body email: ConfirmCodeReq
    ): Call<BaseRes>
}