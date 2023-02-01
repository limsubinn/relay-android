package com.example.relay.mypage

import com.example.relay.ui.models.UserProfileListResponse
import com.example.relay.mypage.models.UserProfileResponse

interface MypageInterface {
    fun onGetUserProfileSuccess(response: UserProfileResponse)
    fun onGetUserProfileFailure(message: String)
}