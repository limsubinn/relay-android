package com.example.relay.timetable

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.BaseResponse
import com.example.relay.timetable.models.*
import retrofit2.Call
import retrofit2.Response

class TimetableService(val timetableInterface: TimetableInterface) {
    private val retrofit: TimetableRetrofit = ApplicationClass.sRetrofit.create(TimetableRetrofit::class.java)

    fun tryPostMySchedules(profileIdx:Long, scheduleList: MutableList<Schedule>){
        retrofit.postMyTimetableReq(profileIdx, MySchedulesReq(scheduleList)).enqueue((object : retrofit2.Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) { // response.code == 200
                    timetableInterface.onPostMyTimetableSuccess()
                }else {
                    Log.d("Timetable", "failure")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetableInterface.onPostMyTimetableFailure(t.message ?: "통신 오류")
            }

        }))
    }

    fun tryGetGroupSchedules(clubIdx: Long){
        retrofit.getGroupTimetablesReq(clubIdx).enqueue((object : retrofit2.Callback<GroupTimetableRes>{
            override fun onResponse(call: Call<GroupTimetableRes>, response: Response<GroupTimetableRes>) {
                if (response.isSuccessful)  // response.code == 200
                    timetableInterface.onGetGroupTimetableSuccess(response.body() as GroupTimetableRes)
                else
                    Log.d("Timetable", "tryGetGroupSchedules failure")
            }

            override fun onFailure(call: Call<GroupTimetableRes>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetableInterface.onGetGroupTimetableFailure(t.message ?: "통신 오류")
            }
        }))
    }

    fun tryGetMySchedules(profileIdx: Long){
        retrofit.getMyTimetableReq(profileIdx).enqueue((object : retrofit2.Callback<MyTimetableRes>{
            override fun onResponse(call: Call<MyTimetableRes>, response: Response<MyTimetableRes>) {
                if (response.isSuccessful)  // response.code == 200
                    timetableInterface.onGetMyTimetableSuccess(response.body() as MyTimetableRes)
                else
                    Log.d("Timetable", "tryGetMySchedules failure")
            }

            override fun onFailure(call: Call<MyTimetableRes>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetableInterface.onGetGroupTimetableFailure(t.message ?: "통신 오류")
            }
        }))
    }
}