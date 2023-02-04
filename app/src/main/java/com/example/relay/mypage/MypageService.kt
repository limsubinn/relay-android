package com.example.relay.mypage

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.mypage.models.ChangePwdRequest
import com.example.relay.mypage.models.ChangePwdResponse
import com.example.relay.mypage.models.DailyRecordResponse
import com.example.relay.mypage.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageService(val mypageInterface: MypageInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryGetUserProfile(profileIdx: Long, name: String){
        retrofit.getProfileRes(profileIdx, name).enqueue(object : Callback<UserProfileResponse>{
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                Log.d("UserProfileResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    mypageInterface.onGetUserProfileSuccess(response.body() as UserProfileResponse)
                } else {
                    Log.d("UserProfileResponse", response.message())
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

    fun tryGetDailyRecord(date: String){
        retrofit.getDailyRes(date).enqueue(object : Callback<DailyRecordResponse>{
            override fun onResponse(call: Call<DailyRecordResponse>, response: Response<DailyRecordResponse>) {
                Log.d("DailyRecordResponse", "success")

                if (response.code() == 200) {
                    mypageInterface.onGetDailyRecordSuccess(response.body() as DailyRecordResponse)
                } else {
                    Log.d("DailyRecordResponse", response.message())
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<DailyRecordResponse>, t: Throwable) {
                Log.d("DailyRecordResponse", "fail")
                t.printStackTrace()
                mypageInterface.onGetDailyRecordFailure(t.message ?: "통신 오류")
            }
        })
    }
}

class MySettingService(val mySettingInterface: MySettingInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryPatchUserPwd(newPwd: String, checkPwd: String){
        retrofit.patchUserPwdReq(ChangePwdRequest(newPwd, checkPwd)).enqueue(object : Callback<ChangePwdResponse>{
            override fun onResponse(call: Call<ChangePwdResponse>, response: Response<ChangePwdResponse>) {
                Log.d("ChangePwdResponse", "success")

                if (response.code() == 200) {
                    mySettingInterface.onPatchUserPwdSuccess(response.body() as ChangePwdResponse)
                } else {
                    Log.d("ChangePwdResponse", response.message())
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<ChangePwdResponse>, t: Throwable) {
                Log.d("ChangePwdResponse", "fail")
                t.printStackTrace()
                mySettingInterface.onPatchUserPwdFailure(t.message ?: "통신 오류")
            }
        })
    }
}