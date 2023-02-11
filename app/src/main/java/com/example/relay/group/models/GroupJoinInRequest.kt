package com.example.relay.group.models

import com.example.relay.timetable.models.Schedule

data class GroupJoinInRequest(
    val userProfileIdx:Long,
    val timeTables:List<Schedule>
)
