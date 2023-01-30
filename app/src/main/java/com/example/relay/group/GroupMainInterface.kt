package com.example.relay.group

import com.example.relay.group.models.GroupAcceptedResponse

interface GroupMainInterface {
    fun onGetUserClubSuccess(response: GroupAcceptedResponse)
    fun onGetUserClubFailure(message: String)
}