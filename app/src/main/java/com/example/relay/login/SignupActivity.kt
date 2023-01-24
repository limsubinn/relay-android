package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private val viewBinding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // 개인정보이용동의 약관 액티비티로 전송 ( or sharedPreferences 사용 )
        viewBinding.btnNext.setOnClickListener{
            val name = viewBinding.etName.text.toString()
            val email = viewBinding.etEmail.text.toString()
            val phone = viewBinding.etPhone.text.toString()
            val pw = viewBinding.etPw.text.toString()
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS;

            if (name.isBlank() || email.isBlank() || phone.isBlank() || pw.isBlank() || viewBinding.etCheckPw.text.toString().isBlank())
                Toast.makeText(this, "입력되지 않은 칸이 존재합니다.", Toast.LENGTH_SHORT).show()
            else if (!emailPattern.matcher(email).matches())
                Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            else if (pw.length < 8)
                Toast.makeText(this, "비밀번호는 8글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            else if (pw != viewBinding.etCheckPw.text.toString()){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PrivacyConditionActivity::class.java)
                val editor = prefs.edit()

                editor.putString("name", name)
                editor.putString("email", email)
                editor.putString("phone", phone)
                editor.putString("pw", pw)
                editor.apply()

                startActivity(intent)
            }
        }
    }
}