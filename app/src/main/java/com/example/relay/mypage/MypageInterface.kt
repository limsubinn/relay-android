package com.example.relay.mypage

import com.example.relay.mypage.models.ChangeMsgResponse
import com.example.relay.mypage.models.ChangePwdResponse
import com.example.relay.mypage.models.DailyRecordResponse
import com.example.relay.mypage.models.UserProfileResponse

interface MypageInterface {
    fun onGetUserProfileSuccess(response: UserProfileResponse)
    fun onGetUserProfileFailure(message: String)

    fun onGetDailyRecordSuccess(response: DailyRecordResponse)
    fun onGetDailyRecordFailure(message: String)
}

interface MySettingInterface {
    fun onPatchUserMsgSuccess(response: ChangeMsgResponse)
    fun onPatchUserMsgFailure(message: String)

    fun onPatchUserPwdSuccess(response: ChangePwdResponse)
    fun onPatchUserPwdFailure(message: String)
}