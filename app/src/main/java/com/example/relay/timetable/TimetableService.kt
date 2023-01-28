package com.example.relay.timetable

import android.util.Log
import com.example.relay.ApplicationClass
import com.example.relay.BaseResponse
import com.example.relay.timetable.models.Schedule
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TimetableService(val timetableInterface: TimetableInterface) {
    private val retrofit: TimetableRetrofit = ApplicationClass.sRetrofit.create(TimetableRetrofit::class.java)

    fun tryPostMySchedules(clubIdx:Int, timetableIdx: Int, scheduleList: MutableList<Schedule>){
        retrofit.postMyTimetable(clubIdx, timetableIdx, scheduleList).enqueue((object : retrofit2.Callback<BaseResponse> {
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
}