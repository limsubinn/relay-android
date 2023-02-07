package com.example.relay.ui.service

import com.example.relay.ui.models.UserInfoResponse
import com.example.relay.ui.models.UserProfileListResponse

interface MainInterface {
    fun onGetUserInfoSuccess(response: UserInfoResponse)
    fun onGetUserInfoFailure(message: String)

    fun onGetProfileListSuccess(response: UserProfileListResponse)
    fun onGetProfileListFailure(message: String)
}