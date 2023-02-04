package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class Member (
    @SerializedName("memberStatusIdx") val statusIdx: Long,
    @SerializedName("runningRecord") val record: ArrayList<RecordWithoutLocation>,
    @SerializedName("userProfile") val profile: MemberProfile
)