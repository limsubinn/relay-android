package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class GroupInfoResult (
    @SerializedName("clubIdx") val clubIdx: Long,
    @SerializedName("content") val content: String,
    @SerializedName("getMemberOfClubResList") val member: ArrayList<Member>,
    @SerializedName("goal") val goal: Float,
    @SerializedName("goalType") val goalType: String,
    @SerializedName("hostIdx") val hostIdx: Long,
    @SerializedName("imgURL") val imgUrl: String,
    @SerializedName("level") val level: Int,
    @SerializedName("maxNum") val maxNum: Int,
    @SerializedName("name") val name: String,
    @SerializedName("recruitStatus") val recruitStatus: String
)