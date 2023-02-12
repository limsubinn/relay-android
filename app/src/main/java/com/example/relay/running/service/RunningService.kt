package com.example.relay.running.service

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.running.models.*
import com.example.relay.timetable.models.MyTimetableRes
import com.example.relay.timetable.service.TimetableRetrofit
import retrofit2.Call
import retrofit2.Response

class RunningService(val runningInterface: RunningInterface) {

    private val retrofit: RunningRetrofit = ApplicationClass.sRetrofit.create(RunningRetrofit::class.java)
    private val retrofit2: TimetableRetrofit = ApplicationClass.sRetrofit.create(TimetableRetrofit::class.java)

    fun tryPostRunStart(profileIdx:Long){
        retrofit.postRunStrRes(RunStrRequest(profileIdx)).enqueue((object : retrofit2.Callback<RunStrResponse> {
            override fun onResponse(call: Call<RunStrResponse>, response: Response<RunStrResponse>) {
                Log.d("RunStart", "success")
                if (response.isSuccessful) { // response.code == 200
                    runningInterface.onPostRunStrSuccess(response.body() as RunStrResponse)
                }else {
                    Log.d("RunStart", "failure")
                }
            }

            override fun onFailure(call: Call<RunStrResponse>, t: Throwable) {
                Log.d("RunStart", t.message!!)
                t.printStackTrace()
                runningInterface.onPostRunStrFailure(t.message ?: "통신 오류")
            }
        }))
    }

    fun tryPostRunEnd(distance: Float, locationList: MutableList<PathPoints>, pace: Float, runningRecordIdx: Long, time: String){
        retrofit.postRunEndRes(RunEndRequest(distance, locationList, pace, runningRecordIdx, time)).enqueue((object : retrofit2.Callback<RunEndResponse> {
            override fun onResponse(call: Call<RunEndResponse>, response: Response<RunEndResponse>) {
                Log.d("RunEnd", "success")
                if (response.isSuccessful) { // response.code == 200
                    runningInterface.onPostRunEndSuccess(response.body() as RunEndResponse)
                }else {
                    Log.d("RunEnd", "failure")
                }
            }

            override fun onFailure(call: Call<RunEndResponse>, t: Throwable) {
                Log.d("RunEnd", t.message!!)
                t.printStackTrace()
                runningInterface.onPostRunEndFailure(t.message ?: "통신 오류")
            }
        }))
    }

    fun tryGetMySchedules(profileIdx: Long){
        retrofit2.getMyTimetableReq(profileIdx).enqueue((object : retrofit2.Callback<MyTimetableRes>{
            override fun onResponse(call: Call<MyTimetableRes>, response: Response<MyTimetableRes>) {
                if (response.isSuccessful)  // response.code == 200
                    runningInterface.onGetMyTimetableSuccess(response.body() as MyTimetableRes)
                else
                    Log.d("Timetable", "tryGetMySchedules failure")
            }

            override fun onFailure(call: Call<MyTimetableRes>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                runningInterface.onGetMyTimetableFailure(t.message ?: "통신 오류")
            }
        }))
    }
}