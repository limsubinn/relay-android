package com.example.relay.mypage

import com.example.relay.mypage.models.UserInfoResponse
import com.example.relay.mypage.models.UserProfileListResponse
import com.example.relay.mypage.models.UserProfileResponse

interface MypageInterface {
    fun onGetUserInfoSuccess(response: UserInfoResponse)
    fun onGetUserInfoFailure(message: String)

    fun onGetProfileListSuccess(response: UserProfileListResponse)
    fun onGetProfileListFailure(message: String)

    fun onGetUserProfileSuccess(response: UserProfileResponse)
    fun onGetUserProfileFailure(message: String)
}