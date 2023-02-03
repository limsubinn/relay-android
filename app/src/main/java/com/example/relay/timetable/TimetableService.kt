package com.example.relay.timetable

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.BaseResponse
import com.example.relay.timetable.models.*
import retrofit2.Call
import retrofit2.Response

class TimetableService(val timetableInterface: TimetableInterface) {
    private val retrofit: TimetableRetrofit = ApplicationClass.sRetrofit.create(TimetableRetrofit::class.java)

    fun tryPostMySchedules(clubIdx:Long, scheduleList: MutableList<Schedule>){
        retrofit.postMyTimetableReq(clubIdx, MySchedulesReq(scheduleList)).enqueue((object : retrofit2.Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) { // response.code == 200
                    timetableInterface.onPostMyTimetableSuccess()
                }else {
                    // 전송은 성공 but 4xx 에러
                    Log.d("LogInLocal", "failure")
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
                if (response.isSuccessful) { // response.code == 200
                    timetableInterface.onGetGroupTimetableSuccess(response.body() as GroupTimetableRes)
                } else {
                    // 전송은 성공 but 4xx 에러
                    Log.d("LogInLocal", "failure")
                }
            }

            override fun onFailure(call: Call<GroupTimetableRes>, t: Throwable) {
                Log.d("Timetable", t.message!!)
                t.printStackTrace()
                timetableInterface.onGetGroupTimetableFailure(t.message ?: "통신 오류")
            }
        }))
    }
}