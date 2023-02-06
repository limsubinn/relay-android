package com.example.relay.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.relay.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private val viewBinding:ActivityAuthenticationBinding by lazy{
        ActivityAuthenticationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnBack.setOnClickListener{
            finish()
        }

        viewBinding.btnCheck.setOnClickListener{
            // ! 연결 액티비티 확인 후 변경 필요 !
            val intent = Intent(this, NewPwActivity::class.java)
            Toast.makeText(this, "인증 기능 없음.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}