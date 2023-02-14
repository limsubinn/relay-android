package com.example.relay.timetable.service

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.BaseResponse
import com.example.relay.timetable.models.*
import retrofit2.Call
import retrofit2.Response

class TimetableGetService(val timetableGetInterface: TimetableGetInterface) {
    private val retrofit: TimetableRetrofit = ApplicationClass.sRetrofit.create(TimetableRetrofit::class.java)

    fun tryGetGroupSchedules(clubIdx: Long){
        retrofit.getGroupTimetablesReq(clubIdx).enqueue((object : retrofit2.Callback<GroupTimetableRes>{
            override fun onResponse(call: Call<GroupTimetableRes>, response: Response<GroupTimetableRes>) {
                if (response.isSuccessful)  // response.code == 200
                    timetableGetInterface.onGetGroupTimetableSuccess(response.body() as GroupTimetableRes)
                else
                    timetableGetInterface.onGetGroupTimetableFailure(response.message())
            }

            override fun onFailure(call: Call<GroupTimetableRes>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetableGetInterface.onGetGroupTimetableFailure(t.message ?: "통신 오류")
            }
        }))
    }

    fun tryGetMySchedules(profileIdx: Long){
        retrofit.getMyTimetableReq(profileIdx).enqueue((object : retrofit2.Callback<MyTimetableRes>{
            override fun onResponse(call: Call<MyTimetableRes>, response: Response<MyTimetableRes>) {
                if (response.isSuccessful)  // response.code == 200
                    timetableGetInterface.onGetMyTimetableSuccess(response.body() as MyTimetableRes)
                else
                    timetableGetInterface.onGetMyTimetableFailure(response.message())
            }

            override fun onFailure(call: Call<MyTimetableRes>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetableGetInterface.onGetGroupTimetableFailure(t.message ?: "통신 오류")
            }
        }))
    }
}

class TimetablePostService(val timetablePostInterface: TimetablePostInterface){
    private val retrofit: TimetableRetrofit = ApplicationClass.sRetrofit.create(TimetableRetrofit::class.java)

    fun tryPostMySchedules(profileIdx:Long, scheduleList: MutableList<Schedule>){
        retrofit.postMyTimetableReq(profileIdx, MySchedulesReq(scheduleList)).enqueue((object : retrofit2.Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) { // response.code == 200
                    when (response.body()?.code){
                        1000 -> timetablePostInterface.onPostMyTimetableSuccess()
                        4000 -> timetablePostInterface.timetableDuplicated()
                        else -> timetablePostInterface.onPostMyTimetableFailure(response.message())
                    }
                }else {
                    timetablePostInterface.onPostMyTimetableFailure(response.message())
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetablePostInterface.onPostMyTimetableFailure(t.message ?: "통신 오류")
            }

        }))
    }
}