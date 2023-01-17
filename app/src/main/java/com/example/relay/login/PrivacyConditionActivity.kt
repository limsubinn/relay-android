package com.example.relay.login

import android.content.Intent
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

        viewBinding.btnAgree.setOnClickListener{
            val intent = Intent(this, LastCheckActivity::class.java)
            startActivity(intent)
        }
    }
}