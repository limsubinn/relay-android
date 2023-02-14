package com.example.relay.group.models

data class MemberRunningStatus (
    val userIdx: Long,
    val nickname: String,
    val imgUrl: String,
    val startTime: Int,
    val endTime: Int,
    val runningStatus: Boolean
)