package com.example.relay.login

import com.example.relay.login.data.*
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface LoginService {
    @POST("users/logIn")
    fun logInLocal(@Body Info: LogInLocalReq): Call<BaseRes>

    @POST("users/sign-in")
    fun signInLocal(@Body Info: SignInLocalReq): Call<BaseRes>

    @POST("users/logIn/Google")
    fun logInGoogle(): Call<BaseRes>

    @POST("users/logIn/Naver")
    fun logInNaver(): Call<BaseRes>

    @POST("users/logIn/Kakao")
    fun logInKakao(): Call<BaseRes>

    @GET("users/findPwd")
    fun findPw(): Call<BaseRes>

    @POST("users/resetPwd")
    fun resetPw(): Call<BaseRes>

    @PATCH("users/profilePwd")
    fun changePw(): Call<BaseRes>
}