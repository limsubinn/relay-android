package com.example.relay.running.service

import com.example.relay.running.models.MainRunningResponse
import com.example.relay.running.models.RunEndResponse
import com.example.relay.running.models.RunStrResponse
import com.example.relay.timetable.models.MyTimetableRes

interface RunningInterface {
    fun onPostRunStrSuccess(response: RunStrResponse)
    fun onPostRunStrFailure(message: String)

    fun onPostRunEndSuccess(response: RunEndResponse)
    fun onPostRunEndFailure(message: String)

}

interface MainInterface {
    fun onGetRunMainSuccess(response: MainRunningResponse)
    fun onGetRunEndFailure(message: String)
}