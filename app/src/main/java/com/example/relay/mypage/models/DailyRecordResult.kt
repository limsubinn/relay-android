package com.example.relay.mypage.models

import com.google.gson.annotations.SerializedName

data class DailyRecordResult (
    @SerializedName("clubName") val clubName: String,
    @SerializedName("date") val date: String,
    @SerializedName("distance") val distance: Float,
    @SerializedName("goalStatus") val goalStatus: String,
    @SerializedName("goalType") val goalType: String,
    @SerializedName("goalValue") val goalValue: Float,
    @SerializedName("locationList") val locationList: ArrayList<LocationList>,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("pace") val pace: Float,
    @SerializedName("recordIdx") val recordIdx: Long,
    @SerializedName("time") val time: Float
)
