package com.example.relay.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.relay.databinding.ActivityNewPwBinding

class NewPwActivity : AppCompatActivity() {
    private val viewBinding: ActivityNewPwBinding by lazy{
        ActivityNewPwBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}