package com.example.relay.running.models

data class RunEndRequest(
    val distance: Float,
    val locations: List<PathPoints>,
    val pace: Float,
    val runningRecordIdx: Long,
    val time: String
)
