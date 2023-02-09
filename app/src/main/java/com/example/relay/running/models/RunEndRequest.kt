package com.example.relay.running.models

data class RunEndRequest(
    val distance: Long,
    val location: List<PathPoints>,
    val pace: Long,
    val runningRecordIdx: Long,
    val time: String
)
