package com.example.relay.ui

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.OnBottomSheetCallbacks
import com.example.relay.R
import com.example.relay.databinding.ActivityMainBinding
import com.example.relay.group.view.*
import com.example.relay.mypage.MainService
import com.example.relay.mypage.MyRecordFragment
import com.example.relay.mypage.MypageFragment
import com.example.relay.running.RunningFragment
import com.example.relay.timetable.view.TimetableFragment
import com.example.relay.ui.models.UserInfoResponse
import com.example.relay.ui.models.UserProfileListResponse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainInterface {
    private var listener: OnBottomSheetCallbacks? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 유저 정보 받아오기
        MainService(this).tryGetUserInfo()

        supportActionBar?.elevation = 0f

        //configureBackdrop()

        supportFragmentManager
            .beginTransaction()
            .replace(binding.containerFragment.id, RunningFragment())
            .commitAllowingStateLoss()

        binding.navBottom.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_mypage -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(binding.containerFragment.id, MypageFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.menu_running -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(binding.containerFragment.id, RunningFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.menu_group -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(binding.containerFragment.id, GroupMainFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.menu_timetable -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(binding.containerFragment.id, TimetableFragment(), "MainTimetable")
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
            selectedItemId = R.id.menu_running
        }
    }

    fun setOnBottomSheetCallbacks(onBottomSheetCallbacks: OnBottomSheetCallbacks) {
        this.listener = onBottomSheetCallbacks
    }

    fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        Log.d("mBottomSheetBehavior","${mBottomSheetBehavior?.state}")
    }

    fun openBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }


//    private fun configureBackdrop() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)
//
//        fragment?.view?.let {
//            BottomSheetBehavior.from(it).let { bs ->
//                bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//
//                    override fun onStateChanged(bottomSheet: View, newState: Int) {
//                        listener?.onStateChanged(bottomSheet, newState)
//                        Log.d("stateChange","${listener}")
//                    }
//                })
//
//                bs.state = BottomSheetBehavior.STATE_COLLAPSED
//                Log.d("BottomSheetBehavior2","${mBottomSheetBehavior}")
//                mBottomSheetBehavior = bs
//            }
//        }
//    }

    // 인덱스를 통해 해당되는 프래그먼트를 띄운다.
    fun mypageFragmentChange(index: Int) {
        if (index == 0) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, MypageFragment())
                .commitAllowingStateLoss()
        } else if (index == 1) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, MyRecordFragment())
                .commitAllowingStateLoss()
        }
    }

    fun groupFragmentChange(index: Int) {
        if (index == 0) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, GroupMainFragment())
                .commitAllowingStateLoss()
        } else if (index == 1) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, GroupListFragment())
                .commitAllowingStateLoss()
        } else if (index == 2) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, GroupMemberFragment())
                .commitAllowingStateLoss()
        } else if (index == 3) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, GroupCreateFragment())
                .commitAllowingStateLoss()
        } else if (index == 4) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, GroupCreateNFragment())
                .commitAllowingStateLoss()
        }
    }

    fun refreshTimetableFragment() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(binding.containerFragment.id, TimetableFragment(), "MainTimetable")
            .commitAllowingStateLoss()
        Log.d("Timetable", "refreshTimetableFragment: complete")
    }

    fun reload(){
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onGetUserInfoSuccess(response: UserInfoResponse) {
        val name = response.result.name
        val email = response.result.email

        val editor = prefs.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.apply()

        // 유저 프로필 리스트 가져오기
        MainService(this).tryGetProfileList(name)
    }

    override fun onGetUserInfoFailure(message: String) {
        Toast.makeText(this, "유저 정보를 받아오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onGetProfileListSuccess(response: UserProfileListResponse) {
        val res = response.result[1] // 수정 필요 !!!
        val userIdx = res.userIdx

        val editor = prefs.edit()
        editor.putLong("userIdx", userIdx)
        editor.apply()
    }

    override fun onGetProfileListFailure(message: String) {
        TODO("Not yet implemented")
    }
}


