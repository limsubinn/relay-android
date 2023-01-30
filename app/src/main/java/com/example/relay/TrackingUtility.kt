package com.example.relay

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.example.relay.service.Polyline
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit
import kotlin.math.round

object TrackingUtility {
    fun hasLocationPermissions(context: Context) =
        //요청할 필요 없는 경우
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }


    //각 polyline 거리 계산
    fun calculatePolylineLength(polyline: Polyline) : Float {
        var distance = 0f
        for (i in 0..polyline.size -2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        distance = round((distance / 1000) * 10) / 100f
        return distance
    }

    fun calculateAvgPace(polyline: Polyline, ms: Long, includeMillis: Boolean) : String {
        var distance = 0f
        var distance2 = 0f
        var speed2 = 0f
        var avgPace = 0f
        var speed = 0f
        if (polyline.size >= 2) {
            for (i in 0..polyline.size -2) {
                val pos1 = polyline[i]
                val pos2 = polyline[i + 1]

                val result = FloatArray(1)
                Location.distanceBetween(
                    pos1.latitude,
                    pos1.longitude,
                    pos2.latitude,
                    pos2.longitude,
                    result
                )
                distance += result[0]
            }
            distance2 = distance
            distance = round((distance / 1000) * 10) / 100f
            speed2 = Math.round((distance2 / 1000f) / (ms / 1000f / 60 / 60) * 10) / 10f
            speed = round((distance / ((TimeUnit.MILLISECONDS.toHours(ms) * 10) / 100f)) * 10) / 100f
            avgPace = round((1 / speed2) * 10) / 100f
            return if (avgPace < 0){
                "0.0"
            } else {
                avgPace.toString()
            }
        } else return "0.0"
    }

    fun calculateNowPace(polyline: Polyline, ms: Long, includeMillis: Boolean) : String {
        var distance = 0f
        var nowPace = 0f
        var speed = 0f
        if (polyline.size >= 2) {
            val pos1 = polyline[polyline.size - 2]
            val pos2 = polyline[polyline.size - 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
            speed = Math.round((distance / 1000f) / (ms / 1000f / 60 / 60) * 10) / 10f
            nowPace = round((1 / speed) * 10) / 100f
            return if (nowPace < 0){
                "0.0"
            } else {
                nowPace.toString()
            }
        } else return "0.0"
    }


    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis) {
            return "${if(hours < 10 ) "0" else ""}$hours:" +
                    "${if(minutes < 10 ) "0" else ""}$minutes:" +
                    "${if(seconds < 10 ) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours < 10 ) "0" else ""}$hours:" +
                "${if(minutes < 10 ) "0" else ""}$minutes:" +
                "${if(seconds < 10 ) "0" else ""}$seconds"
        //"${if(milliseconds < 10 ) "0" else ""}$milliseconds:"
    }
}