package com.example.relay.group.models

data class GroupEditRequest(
    val content: String,
    val goal: Float,
    val goalType: String,
    val imgURL: String,
    val level: Int,
    val maxNum: Int,
    val name: String,
    val recruitStatus: String
)
