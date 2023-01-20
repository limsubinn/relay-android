package com.example.relay.login.response

data class LocalLogInRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<String>
)
