package com.example.relay.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.relay.OnBottomSheetCallbacks
import com.example.relay.R
import com.example.relay.databinding.ActivityMainBinding
import com.example.relay.group.GroupFragment
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

    private var mypageFragment: MypageFragment? = null
    private var runningFragment: RunningFragment? = null
    private var groupFragment: GroupFragment? = null
    private var timetableFragment: TimetableFragment? = null

    private var myrecordFragment: MyRecordFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        runningFragment = RunningFragment()
        supportActionBar?.elevation = 0f

        //configureBackdrop()

        supportFragmentManager
            .beginTransaction()
            .replace(binding.containerFragment.id, runningFragment!!)
            .commitAllowingStateLoss()

        binding.navBottom.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_mypage -> {
                        if(mypageFragment == null) {
                            mypageFragment = MypageFragment()
                            supportFragmentManager
                                .beginTransaction()
                                .add(binding.containerFragment.id, mypageFragment!!)
                                .commitAllowingStateLoss()
                        } else supportFragmentManager.beginTransaction().show(mypageFragment!!).commitAllowingStateLoss()

                        if (runningFragment != null) supportFragmentManager.beginTransaction().hide(runningFragment!!).commitAllowingStateLoss()

                        if (groupFragment != null) supportFragmentManager.beginTransaction().hide(groupFragment!!).commitAllowingStateLoss()

                        if (timetableFragment != null) supportFragmentManager.beginTransaction().hide(timetableFragment!!).commitAllowingStateLoss()

                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_running -> {
                        if(runningFragment == null) {
                            runningFragment = RunningFragment()
                            supportFragmentManager
                                .beginTransaction()
                                .add(binding.containerFragment.id, runningFragment!!)
                                .commitAllowingStateLoss()
                        } else supportFragmentManager.beginTransaction().show(runningFragment!!).commitAllowingStateLoss()

                        if (mypageFragment != null) supportFragmentManager.beginTransaction().hide(mypageFragment!!).commitAllowingStateLoss()

                        if (groupFragment != null) supportFragmentManager.beginTransaction().hide(groupFragment!!).commitAllowingStateLoss()

                        if (timetableFragment != null) supportFragmentManager.beginTransaction().hide(timetableFragment!!).commitAllowingStateLoss()

                    }
                    R.id.menu_group -> {
                        if(groupFragment == null) {
                            groupFragment = GroupFragment()
                            supportFragmentManager
                                .beginTransaction()
                                .add(binding.containerFragment.id, groupFragment!!)
                                .commitAllowingStateLoss()
                        } else supportFragmentManager.beginTransaction().show(groupFragment!!).commitAllowingStateLoss()

                        if (mypageFragment != null) supportFragmentManager.beginTransaction().hide(mypageFragment!!).commitAllowingStateLoss()

                        if (runningFragment != null) supportFragmentManager.beginTransaction().hide(runningFragment!!).commitAllowingStateLoss()

                        if (timetableFragment != null) supportFragmentManager.beginTransaction().hide(timetableFragment!!).commitAllowingStateLoss()

                    }
                    R.id.menu_timetable -> {
                        if(timetableFragment == null) {
                            timetableFragment = TimetableFragment()
                            supportFragmentManager
                                .beginTransaction()
                                .add(binding.containerFragment.id, timetableFragment!!)
                                .commitAllowingStateLoss()
                        } else supportFragmentManager.beginTransaction().show(timetableFragment!!).commitAllowingStateLoss()

                        if (mypageFragment != null) supportFragmentManager.beginTransaction().hide(mypageFragment!!).commitAllowingStateLoss()

                        if (runningFragment != null) supportFragmentManager.beginTransaction().hide(runningFragment!!).commitAllowingStateLoss()

                        if (groupFragment != null) supportFragmentManager.beginTransaction().hide(groupFragment!!).commitAllowingStateLoss()
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
            if(mypageFragment == null) {
                mypageFragment = MypageFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(binding.containerFragment.id, mypageFragment!!)
                    .commitAllowingStateLoss()
            } else supportFragmentManager.beginTransaction().show(mypageFragment!!).commitAllowingStateLoss()

            if (myrecordFragment != null) supportFragmentManager.beginTransaction().hide(myrecordFragment!!).commitAllowingStateLoss()
        } else if (index == 1) {
            if(myrecordFragment == null) {
                myrecordFragment = MyRecordFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(binding.containerFragment.id, myrecordFragment!!)
                    .commitAllowingStateLoss()
            } else supportFragmentManager.beginTransaction().show(myrecordFragment!!).commitAllowingStateLoss()

            if (mypageFragment != null) supportFragmentManager.beginTransaction().hide(mypageFragment!!).commitAllowingStateLoss()
        }
    }

}


