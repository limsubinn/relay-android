package com.example.relay.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.relay.ApplicationClass.Companion.sRetrofit
import com.example.relay.BaseResponse
import com.example.relay.R
import com.example.relay.fcm.data.UserDeviceTokenReq
import com.example.relay.fcm.data.UserDeviceTokenRes
import com.example.relay.mypage.view.MySettingsActivity
import com.example.relay.ui.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FireBaseService() : FirebaseMessagingService(){
    private val TAG = "FirebaseService"
    private val CHANNEL_ID = "RELAY"
    private val CHANNEL_NAME = "RELAY RUN CHANNEL"
    private val CHANNEL_DESCRIPTION = "RELAY RUN NOTIFICATION CHANNEL"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply{
                description = CHANNEL_DESCRIPTION
            }
            val manager : NotificationManager = getSystemService(
                    NOTIFICATION_SERVICE,
                ) as NotificationManager
            manager.createNotificationChannel(channel)
            Log.d(TAG, "created channel named ${CHANNEL_NAME}")
        }else
            Log.d(TAG, "no need to create channel")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"new Token : $token");
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            Log.d(TAG, "message data payload : ${message.data}")
        }
        var notificationInfo:Map<String, String> = mapOf()
        message.notification?.let{
            Log.d(TAG,"message notification body : ${it.body}")
            notificationInfo = mapOf(
                "title" to it.title.toString(),
                "body" to it.body.toString()
            )
            sendNotification(notificationInfo)
        }
    }


    private fun sendNotification(messageBody: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_IMMUTABLE)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(messageBody["title"])
            .setContentText(messageBody["body"])
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)

        createNotificationChannel();
        val notificationManager : NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build());
    }
}