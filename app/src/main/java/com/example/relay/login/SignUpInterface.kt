package com.example.relay.login

import com.example.relay.login.data.SignUpLocalRes

interface SignUpInterface {
    fun onPostLocalSignUpInSuccess(response: SignUpLocalRes)
    fun onPostLocalSignUpInFailure(message: String)
}