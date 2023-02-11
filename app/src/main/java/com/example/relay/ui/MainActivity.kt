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
import com.example.relay.running.OnBottomSheetCallbacks
import com.example.relay.R
import com.example.relay.databinding.ActivityMainBinding
import com.example.relay.group.view.*
import com.example.relay.ui.service.MainService
import com.example.relay.mypage.view.MyRecordFragment
import com.example.relay.mypage.view.MypageFragment
import com.example.relay.timetable.view.TimetableEditMainFragment
import com.example.relay.running.view.RunningFragment
import com.example.relay.timetable.view.TimetableFragment
import com.example.relay.ui.models.UserInfoResponse
import com.example.relay.ui.models.UserProfileListResponse
import com.example.relay.ui.service.MainInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

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

        var bundle = intent.extras

        if (bundle != null) { // 상태메시지 변경 후 Back 버튼
            val msg = bundle.getString("msg")

            if (msg != null) {
                binding.navBottom.selectedItemId = R.id.menu_mypage
                supportFragmentManager
                    .beginTransaction()
                    .replace(binding.containerFragment.id, MypageFragment())
                    .commitAllowingStateLoss()
            }
        } else {
            binding.navBottom.selectedItemId = R.id.menu_running
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, RunningFragment())
                .commitAllowingStateLoss()
        }

        supportActionBar?.elevation = 0f

        //configureBackdrop()

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
                            .replace(
                                binding.containerFragment.id,
                                TimetableFragment(),
                                "MainTimetable"
                            )
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
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
                .replace(binding.containerFragment.id, MemberListFragment())
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
        } else if (index == 5) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, MemberPageFragment())
                .commitAllowingStateLoss()
        } else if (index == 6) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, GroupRecordFragment())
                .commitAllowingStateLoss()
        } else if (index == 7) {
            supportFragmentManager
                .beginTransaction()
                .add(binding.containerFragment.id, GroupTimetableFragment())
                .commitAllowingStateLoss()
        } else if (index == 8) {
            supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFragment.id, MemberRecordFragment())
                .commitAllowingStateLoss()
        }
    }

    fun timetableFragmentChange(index: Int) {
        when(index){
            0 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(binding.containerFragment.id, TimetableFragment())
                    .commitAllowingStateLoss()
            }
            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(binding.containerFragment.id, TimetableEditMainFragment())
                    .commitAllowingStateLoss()
            }
        }
    }

    fun refreshTimetableFragment() {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(binding.containerFragment.id, TimetableFragment(), "MainTimetable")
            .commitAllowingStateLoss()
    }

    fun reloadActivity(){
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
        val res = response.result[0] // 기본 프로필
        val userIdx = res.userIdx

        val editor = prefs.edit()
        editor.putLong("userIdx", userIdx)
        editor.apply()
    }

    override fun onGetProfileListFailure(message: String) {
        TODO("Not yet implemented")
    }
}