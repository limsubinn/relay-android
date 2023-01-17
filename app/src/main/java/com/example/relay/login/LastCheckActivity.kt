package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.relay.MainActivity

import com.example.relay.databinding.ActivityLastCheckBinding

class LastCheckActivity : AppCompatActivity() {
    private val viewBinding: ActivityLastCheckBinding by lazy{
        ActivityLastCheckBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnBack.setOnClickListener{
            finish()
        }

        viewBinding.btnNext.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()        // 스택에 쌓인 액티비티 비우기
            startActivity(intent)
        }
    }
}