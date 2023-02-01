package com.example.relay.mypage

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.ui.models.UserInfoResponse
import com.example.relay.ui.MainInterface
import com.example.relay.ui.MainRetrofit
import com.example.relay.ui.models.UserProfileListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainService(val mainInterface: MainInterface) {

    private val retrofit: MainRetrofit = ApplicationClass.sRetrofit.create(MainRetrofit::class.java)

    fun tryGetUserInfo() {
        retrofit.getUserRes().enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                Log.d("UserInfoResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    mainInterface.onGetUserInfoSuccess(response.body() as UserInfoResponse)
                } else {
                    Log.d("UserInfoResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("UserInfoResponse", "fail")
                t.printStackTrace()
                mainInterface.onGetUserInfoFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryGetProfileList(name: String) {
        retrofit.getProfileListRes(name).enqueue(object : Callback<UserProfileListResponse> {
            override fun onResponse(
                call: Call<UserProfileListResponse>,
                response: Response<UserProfileListResponse>
            ) {
                Log.d("UserProfileListResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    mainInterface.onGetProfileListSuccess(response.body() as UserProfileListResponse)
                } else {
                    Log.d("UserProfileListResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<UserProfileListResponse>, t: Throwable) {
                Log.d("UserProfileListResponse", "fail")
                t.printStackTrace()
                mainInterface.onGetProfileListFailure(t.message ?: "통신 오류")
            }
        })
    }
}
