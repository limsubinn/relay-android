package com.example.relay.running.service

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.BaseResponse
import com.example.relay.running.models.PathPoints
import com.example.relay.running.models.RunEndRequest
import com.example.relay.running.models.RunStrRequest
import retrofit2.Call
import retrofit2.Response

class RunningService(val runningInterface: RunningInterface) {

    private val retrofit: RunningRetrofit = ApplicationClass.sRetrofit.create(RunningRetrofit::class.java)

    fun tryPostRunStart(profileIdx:Long){
        retrofit.postRunStrRes(RunStrRequest(profileIdx)).enqueue((object : retrofit2.Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("RunStart", "success")
                if (response.isSuccessful) { // response.code == 200
                    runningInterface.onPostRunStrSuccess()
                }else {
                    Log.d("RunStart", "failure")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("RunStart", t.message!!)
                t.printStackTrace()
                runningInterface.onPostRunStrFailure(t.message ?: "통신 오류")
            }
        }))
    }

    fun tryPostRunEnd(distance: Int, locationList: MutableList<PathPoints>, pace: Long, runningRecordIdx: Long, time: String){
        retrofit.postRunEndRes(RunEndRequest(distance, locationList, pace, runningRecordIdx, time)).enqueue((object : retrofit2.Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("RunEnd", "success")
                if (response.isSuccessful) { // response.code == 200
                    runningInterface.onPostRunEndSuccess()
                }else {
                    Log.d("RunEnd", "failure")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("RunEnd", t.message!!)
                t.printStackTrace()
                runningInterface.onPostRunEndFailure(t.message ?: "통신 오류")
            }
        }))
    }
}