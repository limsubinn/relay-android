package com.example.relay.timetable.models

data class Schedule(
    var day:Int,
    var start: String,
    var end: String,
    var goal: Float,
    var goalType: String
)
