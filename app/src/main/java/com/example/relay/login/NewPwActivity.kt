package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.relay.databinding.ActivityNewPwBinding

class NewPwActivity : AppCompatActivity() {
    private val viewBinding: ActivityNewPwBinding by lazy{
        ActivityNewPwBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnNext.setOnClickListener{
            val intent = Intent(this, LoginMainActivity::class.java)
            Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            finishAffinity()        // 스택에 쌓인 액티비티 비우기
            startActivity(intent)
        }
    }
}