package com.example.relay.group

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetUserClubService(val mainInterface: GetUserClubInterface) {

    private val retrofit: GroupRetrofit = ApplicationClass.sRetrofit.create(GroupRetrofit::class.java)

    fun tryGetUserClub(id: Long){
        retrofit.getUserProfileClubRes(id).enqueue(object : Callback<GroupAcceptedResponse>{
            override fun onResponse(call: Call<GroupAcceptedResponse>, response: Response<GroupAcceptedResponse>) {
                Log.d("GroupAcceptedResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    mainInterface.onGetUserClubSuccess(response.body() as GroupAcceptedResponse)
                } else {
                    Log.d("GroupAcceptedResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<GroupAcceptedResponse>, t: Throwable) {
                Log.d("GroupAcceptedResponse", "fail")
                t.printStackTrace()
                mainInterface.onGetUserClubFailure(t.message ?: "통신 오류")
            }
        })
    }
}

class GetClubListService(val listInterface: GetClubListInterface) {

    private val retrofit: GroupRetrofit = ApplicationClass.sRetrofit.create(GroupRetrofit::class.java)

    fun tryGetClubList(search: String){
        retrofit.getClubListRes(search).enqueue(object : Callback<GroupListResponse> {
            override fun onResponse(call: Call<GroupListResponse>, response: Response<GroupListResponse>) {
                Log.d("GroupListResponse", "success")
                // Log.d("UserProfileResponse", response.body().toString())

                if (response.code() == 200) {
                    listInterface.onGetClubListSuccess(response.body() as GroupListResponse)
                } else {
                    Log.d("GroupListResponse", "4xx error")
                    // 서버 통신은 성공했으나 오류 코드 받았을 때
                }
            }

            override fun onFailure(call: Call<GroupListResponse>, t: Throwable) {
                Log.d("GroupListResponse", "fail")
                t.printStackTrace()
                listInterface.onGetClubListFailure(t.message ?: "통신 오류")
            }
        })
    }
}