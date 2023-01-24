package com.example.relay.mypage

import com.example.relay.mypage.models.UserClubResponse
import com.example.relay.mypage.models.UserProfileResponse

interface MypageInterface {
    fun onGetUserProfileSuccess(response: UserProfileResponse)
    fun onGetUserProfileFailure(message: String)

    fun onGetUserClubSuccess(response: UserClubResponse)
    fun onGetUserClubFailure(message: String)
}