package com.example.relay.group.service

import com.example.relay.group.models.GroupAcceptedResponse
import com.example.relay.group.models.GroupInfoResponse
import com.example.relay.group.models.GroupListResponse
import com.example.relay.group.models.MemberResponse

interface GetUserClubInterface {
    fun onGetUserClubSuccess(response: GroupAcceptedResponse)
    fun onGetUserClubFailure(message: String)
}

interface GetClubListInterface {
    fun onGetClubListSuccess(response: GroupListResponse)
    fun onGetClubListFailure(message: String)
}

interface GetClubDetailInterface {
    fun onGetClubDetailSuccess(response: GroupInfoResponse)
    fun onGetClubDetailFailure(message: String)
}

interface GetMemberListInterface {
    fun onGetMemberListSuccess(response: MemberResponse)
    fun onGetMemberListFailure(message: String)
}