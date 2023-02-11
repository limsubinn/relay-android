package com.example.relay.running.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.relay.R
import com.example.relay.databinding.ActivityRunSplashBinding
import com.example.relay.ui.MainActivity

class RunSplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        binding = ActivityRunSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvCount.text = "2"
        }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvCount.text = "3"
        }, 2000)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvCount.setTextSize(1, 100F)
            binding.tvCount.text = "출발!"
        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.container_fragment, RunningFragment())
//                .commit()
        }, 3000)
    }
}