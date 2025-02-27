package com.example.relay.running.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.relay.Constants.ACTION_PAUSE_SERVICE
import com.example.relay.Constants.ACTION_RESUME_SERVICE
import com.example.relay.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.relay.Constants.ACTION_STOP_SERVICE
import com.example.relay.Constants.FASTEST_LOCATION_INTERVAL
import com.example.relay.Constants.LOCATION_UPDATE_INTERVAL
import com.example.relay.Constants.NOTIFICATION_CHANNEL_ID
import com.example.relay.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.relay.Constants.TIMER_UPDATE_INTERVAL
import com.example.relay.running.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    var isFirstRun = true

    //    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val timeRunInSeconds = MutableLiveData<Long>()

//    @Inject
//    lateinit var baseNotificationBuilder: NotificationCompat.Builder
//
//    lateinit var curNotificationBuilder: NotificationCompat.Builder

    companion object {
        var timeRunInMillis = MutableLiveData<Long>()
        var distance = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        //val pathPoints = MutableLiveData<MutableList<MutableList<LatLng>>>()
    }

    fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        distance.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
//        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
//            updateNotificationTrackingState(it)
        })
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
//                ACTION_START_SERVICE -> {
//                    if(isFirstRun) {
//                        startForegroundService()
//                        isFirstRun = false
//                        Log.d("isFirstRun","${isFirstRun}")
//                    }
//                }
//                ACTION_RESUME_SERVICE -> {
//                    Timber.d("Resuming service...")
//                    resumeTimer()
//                }
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                        resumeTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    Log.d("isFirstRun","${isFirstRun}")
                    isFirstRun = true
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L   //총 달린 시간
    private var timeStarted = 0L   //시작 시간
    private var lastSecondTimestamp = 0L
    private var lapDistance = 0L


    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        timeRunInMillis.postValue(0)
        timeRun = 0
        lapTime = 0
        timeRunInSeconds.postValue(0)
        lastSecondTimestamp = 0
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                //달리기 시작으로부터 지금까지 시간
                lapTime = (System.currentTimeMillis() - 3000L) - timeStarted
                //lapTime 새로 설정
                timeRunInMillis.postValue(timeRun + lapTime)
//            val nowSpeed = round((nowDistanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f

                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun resumeTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                //달리기 시작으로부터 지금까지 시간
                lapTime = System.currentTimeMillis() - timeStarted
                //lapTime 새로 설정
                timeRunInMillis.postValue(timeRun + lapTime)
//            val nowSpeed = round((nowDistanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f

                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_MUTABLE)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_MUTABLE)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        curNotificationBuilder.javaClass.getDeclaredField("mAction").apply {
//            isAccessible = true
//            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
//        }
//        curNotificationBuilder = baseNotificationBuilder
//            .addAction(R.drawable.ic_baseline_pause_24, notificationActionText, pendingIntent)
//        notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = com.google.android.gms.location.LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let {
                        locations ->
                    for (location in locations) {
                        addPathPoint(location)

                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    //마지막 polyline으로 부터 다시 연결
    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply{
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {

//        addEmptyPolyline()
        postInitialValues()
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

//        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
//
//        timeRunInSeconds.observe(this, Observer {
//            val notification = curNotificationBuilder
//                .setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000L))
//            notificationManager.notify(NOTIFICATION_ID, notification.build())
//        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


}