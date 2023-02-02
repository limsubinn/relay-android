package com.example.relay.timetable

import com.example.relay.timetable.models.GroupTimetableRes

interface TimetableInterface {
    fun onGetGroupTimetableSuccess(response: GroupTimetableRes)
    fun onGetGroupTimetableFailure(message: String)
    fun onPostMyTimetableSuccess()
    fun onPostMyTimetableFailure(message: String)
}