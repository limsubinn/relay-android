package com.example.relay.running.service

import com.example.relay.running.models.RunStrResponse
import com.example.relay.timetable.models.MyTimetableRes

interface RunningInterface {
    fun onPostRunStrSuccess(response: RunStrResponse)
    fun onPostRunStrFailure(message: String)

    fun onPostRunEndSuccess(response: RunEndResponse)
    fun onPostRunEndFailure(message: String)

    fun onGetMyTimetableSuccess(response: MyTimetableRes)
    fun onGetMyTimetableFailure(message: String)
}