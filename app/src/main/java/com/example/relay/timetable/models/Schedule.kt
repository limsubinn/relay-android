package com.example.relay.timetable.models

data class Schedule(
    val day:Int,
    val startTime: String,
    val endTime: String,
    val goal: Int,
    val goalType: String
)
