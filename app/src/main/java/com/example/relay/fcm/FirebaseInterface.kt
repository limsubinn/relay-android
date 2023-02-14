package com.example.relay.fcm

import com.example.relay.fcm.data.UserDeviceTokenRes

interface FirebaseInterface {
    fun onPostDeleteDeviceSuccess(response: UserDeviceTokenRes)
}