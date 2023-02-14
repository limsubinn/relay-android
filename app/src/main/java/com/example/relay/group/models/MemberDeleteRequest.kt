package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class MemberDeleteRequest (
    @SerializedName("userProfileIdx") val userIdx: Long
)