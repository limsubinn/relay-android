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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                            .replace(binding.containerFragment.id, GroupFragment())
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
            // 처음 실행했을 때 자동으로 menu_home에 해당하는 아이템을 가리킨다.
            selectedItemId = R.id.menu_running
        }
    }
}