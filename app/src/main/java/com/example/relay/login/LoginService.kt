package com.example.relay.login

import com.example.relay.login.data.localLogInData
import com.example.relay.login.response.localLogInRes
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

interface loginService {
    @POST("users/logIn")
    fun logInLocal(@Body Info: localLogInData): Call<localLogInRes>
}