package com.example.relay.login.service

import com.example.relay.login.models.LogInLocalRes

interface LogInInterface {
    fun onPostLocalLogInSuccess(response: LogInLocalRes)
    fun onPostLocalLogInTokenExpire(response: LogInLocalRes)
    fun onPostLocalLogInWrongPwd()
    fun onPostLocalLogInFailure(message: String)
}

interface LogInSNSInterface{

}