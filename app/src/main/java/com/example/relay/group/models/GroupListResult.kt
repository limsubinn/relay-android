package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class GroupListResult (
    @SerializedName("clubIdx") val clubIdx: Long,
    @SerializedName("content") val content: String,
    @SerializedName("imgURL") val imgURL: String,
    @SerializedName("name") val name: String,
    @SerializedName("recruitStatus") val recruitStatus: String
)