package com.example.relay.running.service

import com.example.relay.running.models.RunStrResponse

interface RunningInterface {
    fun onPostRunStrSuccess(response: RunStrResponse)
    fun onPostRunStrFailure(message: String)

    fun onPostRunEndSuccess()
    fun onPostRunEndFailure(message: String)
}