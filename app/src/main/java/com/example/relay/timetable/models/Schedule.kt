package com.example.relay.timetable.models

data class Schedule(
    val day:Int,
    var start: String,
    var end: String,
    val goal: Int,
    var goalType: String
)
