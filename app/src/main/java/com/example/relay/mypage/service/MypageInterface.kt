package com.example.relay.mypage.service

import com.example.relay.mypage.models.*

interface MypageInterface {
    fun onGetUserProfileSuccess(response: UserProfileResponse)
    fun onGetUserProfileFailure(message: String)

    fun onGetDailyRecordSuccess(response: DailyRecordResponse)
    fun onGetDailyRecordFailure(message: String)
}

interface MyRecordInterface {
    fun onGetMonthRecordSuccess(response: MonthRecordResponse)
    fun onGetMonthRecordFailure(message: String)
}

interface MySettingInterface {
    fun onPatchUserMsgSuccess(response: ChangeMsgResponse)
    fun onPatchUserMsgFailure(message: String)

    fun onPatchUserPwdSuccess(response: ChangePwdResponse)
    fun onPatchUserPwdFailure(message: String)
}