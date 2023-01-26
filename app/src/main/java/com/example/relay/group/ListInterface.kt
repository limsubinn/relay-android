package com.example.relay.group

import com.example.relay.group.models.GroupListResponse

interface ListInterface {
    fun onGetClubListSuccess(response: GroupListResponse)
    fun onGetClubListFailure(message: String)
}