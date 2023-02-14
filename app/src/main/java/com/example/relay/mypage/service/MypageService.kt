package com.example.relay.mypage.service

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.BaseResponse
import com.example.relay.group.models.MemberDeleteRequest
import com.example.relay.group.service.GroupRetrofit
import com.example.relay.group.service.PatchMemberInterface
import com.example.relay.mypage.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageService(val mypageInterface: MypageInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryGetUserProfile(profileIdx: Long){
        retrofit.getProfileRes(profileIdx).enqueue(object : Callback<UserProfileResponse>{
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

    fun tryGetDailyRecord(date: String, idx: Long){
        retrofit.getDailyRes(date, idx).enqueue(object : Callback<DailyRecordResponse>{
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

class MyRecordService(val myRecordInterface: MyRecordInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryGetDailyRecord(year: Int, month: Int, profileIdx: Long){
        retrofit.getMonthRes(year, month, profileIdx).enqueue(object : Callback<MonthRecordResponse>{
            override fun onResponse(call: Call<MonthRecordResponse>, response: Response<MonthRecordResponse>) {
                Log.d("MonthRecordResponse", "success")

                if (response.code() == 200) {
                    myRecordInterface.onGetMonthRecordSuccess(response.body() as MonthRecordResponse)
                } else {
                    Log.d("MonthRecordResponse", response.message())
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<MonthRecordResponse>, t: Throwable) {
                Log.d("MonthRecordResponse", "fail")
                t.printStackTrace()
                myRecordInterface.onGetMonthRecordFailure(t.message ?: "통신 오류")
            }
        })
    }
}

class MySettingService(val mySettingInterface: MySettingInterface) {

    private val retrofit: MypageRetrofit = ApplicationClass.sRetrofit.create(MypageRetrofit::class.java)

    fun tryPatchUserMsg(statusMsg: String){
        retrofit.patchUserMsgReq(ChangeMsgRequest(statusMsg)).enqueue(object : Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if(response.code() == 200) {
                    mySettingInterface.onPatchUserMsgSuccess()
                } else {
                    mySettingInterface.onPatchUserMsgFailure(response.message())
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("patchMsg", "failure")
                t.printStackTrace()
                mySettingInterface.onPatchUserMsgFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryPatchUserImg(imgUrl: String){
        retrofit.patchUserImgReq(ChangeImgRequest(imgUrl)).enqueue(object : Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if(response.code() == 200) {
                    mySettingInterface.onPatchUserImgSuccess()
                } else {
                    mySettingInterface.onPatchUserImgFailure(response.message())
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("patchImg", "failure")
                t.printStackTrace()
                mySettingInterface.onPatchUserImgFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryPatchUserPwd(newPwd: String, checkPwd: String){
        retrofit.patchUserPwdReq(ChangePwdRequest(newPwd, checkPwd)).enqueue(object : Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if(response.code() == 200) {
                    mySettingInterface.onPatchUserPwdSuccess()
                } else {
                    mySettingInterface.onPatchUserPwdFailure(response.message())
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("patchPwd", "failure")
                t.printStackTrace()
                mySettingInterface.onPatchUserPwdFailure(t.message ?: "통신 오류")
            }
        })
    }

}