package com.example.relay.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.relay.databinding.ActivityPrivacyConditionBinding

class PrivacyConditionActivity : AppCompatActivity() {
    private val viewBinding: ActivityPrivacyConditionBinding by lazy {
        ActivityPrivacyConditionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}