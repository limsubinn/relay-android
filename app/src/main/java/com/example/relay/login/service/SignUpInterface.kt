package com.example.relay.login.service

import com.example.relay.login.models.SignUpLocalRes
import com.example.relay.login.models.UserProfileRes

interface SignUpInterface {
    // 로컬 회원가입
    fun onPostLocalSignUpInSuccess(response: SignUpLocalRes)
    fun onPostLocalSignUpInFailure(message: String)

    // 프로필 신규 생성
    fun onPostUserProfileSuccess(response: UserProfileRes)
    fun onPostUserProfileFailure(message: String)
}