package com.example.relay.group

import com.example.relay.group.models.GroupListResponse

interface GroupListInterface {
    fun onGetClubListSuccess(response: GroupListResponse)
    fun onGetClubListFailure(message: String)
}