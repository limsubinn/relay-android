package com.example.relay.running.service

interface RunningInterface {
    fun onPostRunStrSuccess()
    fun onPostRunStrFailure(message: String)

    fun onPostRunEndSuccess()
    fun onPostRunEndFailure(message: String)
}