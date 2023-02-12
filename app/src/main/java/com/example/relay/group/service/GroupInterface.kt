package com.example.relay.group.service

import com.example.relay.group.models.*
import com.example.relay.mypage.models.MonthRecordResponse

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

interface GetClubDailyInterface {
    fun onGetClubDailySuccess(response: GroupDailyRecordResponse)
    fun onGetClubDailyFailure(message: String)
}

interface GetClubMonthInterface {
    fun onGetClubMonthSuccess(response: MonthRecordResponse)
    fun onGetClubMonthFailure(message: String)
}

interface PostNewClubInterface {
    fun onPostNewClubSuccess()
    fun onPostNewClubFailure(message: String)
}

interface PostClubJoinInInterface {
    fun onPostClubJoinInSuccess()
    fun onPostClubJoinInFailure(message: String)
}