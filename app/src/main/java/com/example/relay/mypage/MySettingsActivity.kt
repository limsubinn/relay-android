package com.example.relay.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.relay.databinding.ActivityMySettingsBinding

class MySettingsActivity : AppCompatActivity() {
    private val viewBinding: ActivityMySettingsBinding by lazy{
        ActivityMySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnBack.setOnClickListener{
            finish()
        }
    }
}