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
    fun onPatchUserMsgSuccess()
    fun onPatchUserMsgFailure(message: String)

    fun onPatchUserPwdSuccess()
    fun onPatchUserPwdFailure(message: String)

    fun onPatchUserImgSuccess()
    fun onPatchUserImgFailure(message: String)

    fun onPatchUserAlarmSuccess()
    fun onPatchUserAlarmFailure(message: String)
}