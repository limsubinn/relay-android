package com.example.relay.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.relay.OnBottomSheetCallbacks
import com.example.relay.R
import com.example.relay.databinding.ActivityMainBinding
import com.example.relay.group.GroupListFragment
import com.example.relay.group.GroupMainFragment
import com.example.relay.group.GroupMemberFragment
import com.example.relay.mypage.MyRecordFragment
import com.example.relay.mypage.MypageFragment
import com.example.relay.running.RunningFragment
import com.example.relay.timetable.TimetableFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var listener: OnBottomSheetCallbacks? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                            .replace(binding.containerFragment.id, TimetableFragment())
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
}


