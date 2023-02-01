package com.example.relay.group

import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupListResponse

interface GetUserClubInterface {
    fun onGetUserClubSuccess(response: GroupAcceptedResponse)
    fun onGetUserClubFailure(message: String)
}

interface GetClubListInterface {
    fun onGetClubListSuccess(response: GroupListResponse)
    fun onGetClubListFailure(message: String)
}