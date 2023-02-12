package com.example.relay.group.models

import com.example.relay.timetable.models.Schedule

data class GroupNewRequest(
    val content: String,
    val goal: Float,
    val goalType: String,
    val hostIdx: Long,
    val imgUrl: String,
    val level: Int,
    val maxNum: Int,
    val name: String,
    var timeTable: List<Schedule>
)
