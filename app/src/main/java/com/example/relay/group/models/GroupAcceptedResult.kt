package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class GroupAcceptedResult(
    @SerializedName("clubIdx") val clubIdx: Long,
    @SerializedName("name") val name: String
)
