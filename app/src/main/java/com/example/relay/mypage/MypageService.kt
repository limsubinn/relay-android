package com.example.relay.mypage

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.mypage.models.UserClubResponse
import com.example.relay.mypage.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MypageService(val mypageInterface: MypageInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryGetUserProfile(){
        retrofit.getProfileRes().enqueue(object : Callback<UserProfileResponse>{
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

    fun tryGetUserClub(userIdx: Long){
        retrofit.getClubRes(userIdx).enqueue(object : Callback<UserClubResponse>{
            override fun onResponse(call: Call<UserClubResponse>, response: Response<UserClubResponse>) {
                Log.d("UserClubResponse", "success")
                // Log.d("UserClubResponse", response.body().toString())
                if (response.code() == 200) {
                    mypageInterface.onGetUserClubSuccess(response.body() as UserClubResponse)
                } else {
                    Log.d("UserClubResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<UserClubResponse>, t: Throwable) {
                Log.d("UserClubResponse", "fail")
                t.printStackTrace()
                mypageInterface.onGetUserClubFailure(t.message ?: "통신 오류")
            }
        })
    }
}
