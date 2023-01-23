package com.example.relay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.relay.databinding.ActivityMainBinding
import com.example.relay.group.GroupFragment
import com.example.relay.mypage.MypageFragment
import com.example.relay.running.RunningFragment
import com.example.relay.timetable.TimetableFragment

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mypageFragment: MypageFragment? = null
    private var runningFragment: RunningFragment? = null
    private var groupFragment: GroupFragment? = null
    private var timetableFragment: TimetableFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        runningFragment = RunningFragment()

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
}