package com.example.relay.running.service

import com.example.relay.running.models.RunStrResponse

interface RunningInterface {
    fun onPostRunStrSuccess(response: RunStrResponse)
    fun onPostRunStrFailure(message: String)

    fun onPostRunEndSuccess(response: RunEndResponse)
    fun onPostRunEndFailure(message: String)
}