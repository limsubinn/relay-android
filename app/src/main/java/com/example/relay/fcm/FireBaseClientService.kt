package com.example.relay.fcm

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.fcm.data.UserDeviceTokenReq
import com.example.relay.fcm.data.UserDeviceTokenRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FireBaseClientService(val firebaseInterface: FirebaseInterface) {
    private val retrofit: FireBaseRetrofit = ApplicationClass.sRetrofit.create(FireBaseRetrofit::class.java)
    private val TAG = "FireBaseClientService"

    fun tryPostUserDevice(token: String) {
        retrofit.postDeviceReq(UserDeviceTokenReq(token)).enqueue((object :
            Callback<UserDeviceTokenRes> {
            override fun onResponse(
                call: Call<UserDeviceTokenRes>,
                response: Response<UserDeviceTokenRes>
            ) {
                if(response.isSuccessful) {
                    if (response.body()?.isSuccess == true) {
                        Log.d(TAG, "PostUserDevice success");
//                        firebaseInterface.onPostDeviceSuccess(response.body() as UserDeviceTokenRes)
                    }else{
                        Log.d("PostUserDevice", "통신 성공, ${response.body()?.message}")
                    }
                }else {
                    Log.d("PostUserDevice", "전송 성공 4xx 에러")
                }
            }

            override fun onFailure(call: Call<UserDeviceTokenRes>, t: Throwable) {
                Log.d("PostUserDevice", t.message!!);
                t.printStackTrace();
            }
        }))
    }

    fun tryDeleteUserDevice(token: String) {
        retrofit.deleteDeviceReq(UserDeviceTokenReq(token)).enqueue((object :
            Callback<UserDeviceTokenRes> {
            override fun onResponse(call: Call<UserDeviceTokenRes>, response: Response<UserDeviceTokenRes>) {
                if(response.isSuccessful) {
                    if (response.body()?.isSuccess == true) {
                        Log.d(TAG, "PostUserDevice success")
                        firebaseInterface.onPostDeleteDeviceSuccess(response.body() as UserDeviceTokenRes);
//                        MySettingsActivity().onPostDeleteDeviceSuccess(response.body() as UserDeviceTokenRes)
                    }else{
                        Log.d("DeleteUserDevice", "통신 성공, ${response.body()?.message}")
                    }
                }else {
                    Log.d("DeleteUserDevice", "전송 성공 4xx 에러")
                }
            }

            override fun onFailure(call: Call<UserDeviceTokenRes>, t: Throwable) {
                Log.d("DeleteUserDevice", t.message!!);
                t.printStackTrace();
            }

        }))
    }

}