package com.example.relay.running

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.relay.Constants.ACTION_PAUSE_SERVICE
import com.example.relay.Constants.ACTION_RESUME_SERVICE
import com.example.relay.Constants.ACTION_STOP_SERVICE
import com.example.relay.Constants.MAP_ZOOM
import com.example.relay.Constants.POLYLINE_WIDTH
import com.example.relay.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.relay.OnBottomSheetCallbacks
import com.example.relay.R
import com.example.relay.TrackingUtility
import com.example.relay.databinding.FragmentRunningBinding
import com.example.relay.db.Run
import com.example.relay.service.Polyline
import com.example.relay.service.TrackingService
import com.example.relay.ui.viewmodels.RunningViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.provider.TedPermissionProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_running.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Math.round


@AndroidEntryPoint
class RunningFragment: Fragment(), EasyPermissions.PermissionCallbacks {

    private val viewModel: RunningViewModel by viewModels()
    private lateinit var binding: FragmentRunningBinding
    private var map: GoogleMap? = null
    private lateinit var mapView: MapView

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var curTimeInMillis = 0L

    private var curDistance = 0L

    private var listener: OnBottomSheetCallbacks? = null

    var currentMarker: Marker? = null
    var markerView: View? = null

    var markerIcon = BitmapFactory.decodeResource(
        TedPermissionProvider.context.resources,
        R.drawable.img_marker
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(inflater, container, false)
        requestPermissions()
//        if (Build.VERSION.SDK_INT >= 23) {
//            TedPermission.create()
//                .setPermissionListener(this)
//                .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
//                .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
//                .setDeniedCloseButtonText("닫기")
//                .setGotoSettingButtonText("설정")
//                .setRationaleTitle("HELLO")
//                .setPermissions(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//                .check()
//        }

        mapView = binding.mapView
//        mapView = MapView(requireContext())
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            moveCameraToUser()
            map = it
            addAllPolylines()
        }
//        setCustomMarkerView()


        binding.btnStart1.setOnClickListener {
            startActivity(Intent(context,RunSplashActivity::class.java))
            binding.layoutTimer.visibility = View.VISIBLE
            binding.layoutBottomSheet.visibility = View.VISIBLE
            startRun()
        }

        binding.btnPause.setOnClickListener {
            startRun()
        }

        binding.btnRestart.setOnClickListener {
            startRun()
        }

        binding.btnEnd.setOnClickListener {
            endRunAndSaveToDB()
        }

        binding.tvLookBig.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_running, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
            val  mAlertDialog = mBuilder.show()

            val button = mDialogView.findViewById<ImageView>(R.id.img_close)
                button.setOnClickListener {
                mAlertDialog.dismiss()
            }
            TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
                curTimeInMillis = it
                val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
                mDialogView.findViewById<TextView>(R.id.tv_big_time).text = formattedTime
//            var bigTime = CustomDialog(requireContext()).findViewById<View>(R.id.tv_big_time)
//            bigTime = formattedTime
            })
            TrackingService.pathPoints.observe(viewLifecycleOwner, Observer{
                pathPoints = it
                val formattedDistance = TrackingUtility.calculatePolylineLength(pathPoints.last())
                val formattedAvgDistance = TrackingUtility.calculateAvgPace(pathPoints.last(),curTimeInMillis, true)
                val formattedNowDistance = TrackingUtility.calculateNowPace(pathPoints.last(),curTimeInMillis, true)
                mDialogView.findViewById<TextView>(R.id.tv_big_km).text = formattedDistance.toString()
                mDialogView.findViewById<TextView>(R.id.tv_big_avg_pace).text = formattedAvgDistance
                mDialogView.findViewById<TextView>(R.id.tv_big_now_pace).text = formattedNowDistance
            })
//            CustomDialog(requireContext()).show()
        }

        subscribeToObservers()
//        supportActionBar?.elevation = 0f
//        configureBackdrop()

        return binding.root
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer{
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
            val formattedDistance = TrackingUtility.calculatePolylineLength(pathPoints.last())
            val formattedAvgDistance = TrackingUtility.calculateAvgPace(pathPoints.last(),curTimeInMillis, true)
            val formattedNowDistance = TrackingUtility.calculateNowPace(pathPoints.last(),curTimeInMillis, true)
            binding.tvDistance.text = formattedDistance.toString()
            binding.tvPace2.text = formattedAvgDistance
            binding.tvPace1.text = formattedNowDistance
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedGoalTime = TrackingUtility.getFormattedStopWatchTime(2400000 - curTimeInMillis, true)
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.tvTimer.text = formattedGoalTime
            binding.tvTime.text = formattedTime
        })
    }

    private fun startRun() {
        if(isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
            binding.btnPause.visibility = View.GONE
            binding.layoutWhilePause.visibility = View.VISIBLE
            binding.layoutBottomSheet.visibility = View.VISIBLE
            binding.btnStart1.setImageResource(R.drawable.img_btn_restart)
            //configureBackdrop()
        } else {
            sendCommandToService(ACTION_RESUME_SERVICE)
            binding.btnPause.visibility = View.VISIBLE
            binding.layoutWhilePause.visibility = View.GONE
            binding.layoutBottomSheet.visibility = View.VISIBLE
            binding.btnStart1.setImageResource(R.drawable.img_btn_pause)
            //configureBackdrop()
        }
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        binding.layoutTimer.visibility = View.GONE
        binding.layoutBottomSheet.visibility = View.GONE
        binding.btnPause.visibility = View.VISIBLE
        binding.layoutWhilePause.visibility = View.GONE
        binding.btnStart1.setImageResource(R.drawable.img_btn_start)
        curTimeInMillis = 0
        TrackingService.timeRunInMillis.value = 0
        val time = TrackingService.timeRunInMillis.value
        Log.d("timeRunInMillis", "${time}")
        Log.d("curTimeInMillis", "${curTimeInMillis}")

//        binding.tvTimer.text = "00:00:00"
//        binding.tvTime.text = "00:00:00"
        pathPoints.clear()
        map?.clear()
//        val ft = fragmentManager!!.beginTransaction()
//        ft.detach(this).attach(this).commit()
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            binding.layoutTimer.visibility = View.VISIBLE

        } else {
//            val bottomSheet = WindowFragment()
//            bottomSheet.show(childFragmentManager,bottomSheet.tag)
//            binding.tvStart.text = "중단"
        }
    }



    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun endRunAndSaveToDB() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for(polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
                //binding.tvDistance.text = distanceInMeters.toString()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * 60).toInt()
            val run = Run(bmp, dateTimestamp, avgSpeed, distanceInMeters, curTimeInMillis, caloriesBurned)
            viewModel.insertRun(run)
            val showToast = Toast.makeText(context, "DB 저장",Toast.LENGTH_LONG)
            showToast.show()
            stopRun()
        }
    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.parseColor("#FFFF3E00"))
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)

        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLating = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val markerOptions = MarkerOptions()
            markerOptions.position(lastLatLng)
            val polylineOptions = PolylineOptions()
                .color(Color.parseColor("#FFFF3E00"))
                .width(POLYLINE_WIDTH)
                .add(preLastLating)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
            setCurrentLocation(lastLatLng)

        }
    }



    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //map 생명주기
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    //비동기로 인한 작동 속도 해결 위한 코드
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also{
            it.action = action
            requireContext().startService(it)
        }

    fun setOnBottomSheetCallbacks(onBottomSheetCallbacks: OnBottomSheetCallbacks) {
        this.listener = onBottomSheetCallbacks
    }

    fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

//    private fun configureBackdrop() {
//        val fragment = childFragmentManager.findFragmentById(R.id.filter_fragment)
//
//        fragment?.view?.let {
//            BottomSheetBehavior.from(it).let { bs ->
//                bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//
//                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        listener?.onStateChanged(bottomSheet, newState)
//                    }
//                })
//
//                bs.state = BottomSheetBehavior.STATE_EXPANDED
//                mBottomSheetBehavior = bs
//            }
//        }
//    }

    fun setCurrentLocation(location: LatLng) {
        if (currentMarker != null) currentMarker!!.remove()
        val currentLatLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.draggable(true)
        currentMarker = map?.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon)))
        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        map?.moveCamera(cameraUpdate)
    }

    private fun setCustomMarkerView() {
        markerView = LayoutInflater.from(context).inflate(com.example.relay.R.drawable.marker,null)
    }
}