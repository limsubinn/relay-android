package com.example.relay.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.relay.databinding.ActivityForgetPwBinding

class ForgetPwActivity : AppCompatActivity() {
    private val viewBinding: ActivityForgetPwBinding by lazy{
        ActivityForgetPwBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnSend.setOnClickListener{
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }
}