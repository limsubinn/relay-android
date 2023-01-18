package com.example.relay.running

import android.os.Build
import android.os.Bundle
import android.Manifest
import android.text.style.EasyEditSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.relay.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.relay.TrackingUtility
import com.example.relay.databinding.FragmentRunningBinding
import com.example.relay.db.RunDao
import com.example.relay.ui.viewmodels.RunningViewModel
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_running.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class RunningFragment: Fragment() {

    private val viewModel: RunningViewModel by viewModels()
    private lateinit var binding: FragmentRunningBinding
    private var map: GoogleMap? = null
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(inflater, container, false)
        requestPermission()

        mapView = binding.mapView
//        mapView = MapView(requireContext())
        //map 생명주기
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
        }

        return binding.root
    }

    private fun requestPermission() {
        if(TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        //이전에 허가 안한 경우 다시 반환 요청
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "앱을 사용하려면 위치 정보 수집을 허용해야합니다.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "앱을 사용하려면 위치 정보 수집을 허용해야합니다.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    //위치 정보 동의
//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
//            AppSettingsDialog.Builder(this).build().show()
//        } else {
//            requestPermission()
//        }
//    }

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
}