package com.example.relay.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.R
import com.example.relay.databinding.ActivityMySettingsBinding
import com.example.relay.login.LoginMainActivity
import kotlinx.android.synthetic.main.dialog_change_pw.view.*
import kotlinx.android.synthetic.main.dialog_goal_type.view.*

class MySettingsActivity : AppCompatActivity() {
    private val viewBinding: ActivityMySettingsBinding by lazy{
        ActivityMySettingsBinding.inflate(layoutInflater)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnBack.setOnClickListener {
            finish()
        }

        viewBinding.btnLogout.setOnClickListener {
            // 저장된 계정 내용 초기화
            prefs.edit().clear().apply()
            val intent = Intent(this, LoginMainActivity::class.java)
            finishAffinity()        // 스택에 쌓인 액티비티 비우기
            startActivity(intent)
        }

        // 비밀번호 변경하기
        viewBinding.btnChangePwd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_change_pw, null)
            val alertDialog = AlertDialog.Builder(this).create()

            alertDialog?.setView(dialogView)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()

            dialogView.btn_cancel.setOnClickListener {
                alertDialog?.dismiss()
            }

            dialogView.btn_save.setOnClickListener {
                val pw = prefs.getString("pw", "")
                val beforePw = dialogView.et_before_pw.text.toString()
                val newPw = dialogView.et_new_pw.text.toString()
                val checkPw = dialogView.et_check_pw.text.toString()

                if (beforePw != pw) {
                    Toast.makeText(this, "기존 비밀번호를 확인해주세요!", Toast.LENGTH_SHORT).show()
                }


            }
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