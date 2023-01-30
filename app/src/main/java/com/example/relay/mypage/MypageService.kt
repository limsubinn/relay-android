package com.example.relay.mypage

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.mypage.models.UserInfoResponse
import com.example.relay.mypage.models.UserProfileListResponse
import com.example.relay.mypage.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MypageService(val mypageInterface: MypageInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryGetUserInfo() {
        retrofit.getUserRes().enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                Log.d("UserInfoResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    mypageInterface.onGetUserInfoSuccess(response.body() as UserInfoResponse)
                } else {
                    Log.d("UserInfoResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("UserInfoResponse", "fail")
                t.printStackTrace()
                mypageInterface.onGetUserInfoFailure(t.message ?: "통신 오류")
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
                    mypageInterface.onGetProfileListSuccess(response.body() as UserProfileListResponse)
                } else {
                    Log.d("UserProfileListResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<UserProfileListResponse>, t: Throwable) {
                Log.d("UserProfileListResponse", "fail")
                t.printStackTrace()
                mypageInterface.onGetProfileListFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryGetUserProfile(profileIdx: Long, name: String){
        retrofit.getProfileRes(profileIdx, name).enqueue(object : Callback<UserProfileResponse>{
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                Log.d("UserProfileResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    mypageInterface.onGetUserProfileSuccess(response.body() as UserProfileResponse)
                } else {
                    Log.d("UserProfileResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.d("UserProfileResponse", "fail")
                t.printStackTrace()
                mypageInterface.onGetUserProfileFailure(t.message ?: "통신 오류")
            }
        })
    }
}
