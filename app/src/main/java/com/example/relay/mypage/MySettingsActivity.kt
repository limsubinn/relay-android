package com.example.relay.mypage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.databinding.ActivityMySettingsBinding
import com.example.relay.login.view.LoginMainActivity

class MySettingsActivity : AppCompatActivity() {
    private val viewBinding: ActivityMySettingsBinding by lazy{
        ActivityMySettingsBinding.inflate(layoutInflater)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnBack.setOnClickListener{
            finish()
        }

        viewBinding.btnLogout.setOnClickListener{
            // 저장된 계정 내용 초기화
            prefs.edit().clear().apply()
            val intent = Intent(this, LoginMainActivity::class.java)
            finishAffinity()        // 스택에 쌓인 액티비티 비우기
            startActivity(intent)
        }

        var bundle = intent.extras
        if (bundle != null) {
            val imgUrl = bundle.getString("imgUrl", "")
            val name = bundle.getString("name", "")
            val email = bundle.getString("email", "")
            val statusMsg = bundle.getString("statusMsg", "")
            val isAlarmOn = bundle.getString("isAlarmOn", "")

            Glide.with(viewBinding.imgUser.context)
                .load(imgUrl)
                .into(viewBinding.imgUser)
            viewBinding.tvUserName.text = name
            viewBinding.tvEmail.text = email
            if (!statusMsg.equals("")) {
                viewBinding.tvInfo.text = statusMsg
            }
            viewBinding.swAlarm.isChecked = isAlarmOn.equals("y")

        }
    }
}