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
    private val accessToken: String? = prefs.getString("accessToken", "")

    fun tryGetUserProfile(){
        if (accessToken != null) {
            retrofit.getProfileRes(accessToken).enqueue(object : Callback<UserProfileResponse>{
                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                    Log.d("UserProfileResponse", "success")
                    Log.d("UserProfileResponse", response.body().toString())
                    mypageInterface.onGetUserProfileSuccess(response.body() as UserProfileResponse)
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.d("UserProfileResponse", "fail")
                    t.printStackTrace()
                    mypageInterface.onGetUserProfileFailure(t.message ?: "통신 오류")
                }
            })
        }
    }

    fun tryGetUserClub(userIdx: Long){
        retrofit.getClubRes(userIdx).enqueue(object : Callback<UserClubResponse>{
            override fun onResponse(call: Call<UserClubResponse>, response: Response<UserClubResponse>) {
                Log.d("UserClubResponse", "success")
                Log.d("UserClubResponse", response.body().toString())
                mypageInterface.onGetUserClubSuccess(response.body() as UserClubResponse)
            }

            override fun onFailure(call: Call<UserClubResponse>, t: Throwable) {
                Log.d("UserClubResponse", "fail")
                t.printStackTrace()
                mypageInterface.onGetUserClubFailure(t.message ?: "통신 오류")
            }
        })
    }
}
