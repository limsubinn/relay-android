package com.example.relay.group.models

import com.google.gson.annotations.SerializedName

data class HostChangeRequest (
    @SerializedName("nextHostProfileIdx") val nextIdx: Long
)