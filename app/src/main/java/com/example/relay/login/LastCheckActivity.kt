package com.example.relay.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.relay.databinding.ActivityLastCheckBinding

class LastCheckActivity : AppCompatActivity() {
    private val viewBinding: ActivityLastCheckBinding by lazy{
        ActivityLastCheckBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}