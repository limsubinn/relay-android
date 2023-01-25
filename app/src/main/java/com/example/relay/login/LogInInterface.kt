package com.example.relay.login

import com.example.relay.login.data.LogInLocalRes

interface LogInInterface {
    fun onPostLocalLogInSuccess(response: LogInLocalRes)
    fun onPostLocalLogInTokenExpire(response: LogInLocalRes)
    fun onPostLocalLogInWrongPwd()
    fun onPostLocalLogInFailure(message: String)
}