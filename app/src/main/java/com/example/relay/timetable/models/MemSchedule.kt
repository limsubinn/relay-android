package com.example.relay.timetable.models

data class MemSchedule(
    val timeTableIdx:Long,
    val day:Int,
    val start:String,
    val end:String,
    val goal:Float,
    val goalType:String
)
