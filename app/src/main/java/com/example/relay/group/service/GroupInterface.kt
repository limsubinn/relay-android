package com.example.relay.group.service

import com.example.relay.group.models.*

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

interface GetClubDailyInterface {
    fun onGetClubDailySuccess(response: GroupDailyRecordResponse)
    fun onGetClubDailyFailure(message: String)
}

interface GetMemberListInterface {
    fun onGetMemberListSuccess(response: MemberResponse)
    fun onGetMemberListFailure(message: String)
}