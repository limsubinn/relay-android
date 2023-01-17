package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.relay.databinding.ActivitySignupBinding
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private val viewBinding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // 개인정보이용동의 약관 액티비티로 전송 ( or sharedPreferences 사용 )
        viewBinding.btnNext.setOnClickListener{
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS;
            if (emailPattern.matcher(viewBinding.etEmail.text.toString()).matches()) {
                Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (!viewBinding.etPw.text.toString().equals(viewBinding.etCheckPw.text.toString())){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (false){
                // (미구현) 서버와 통신해서 계정이 있는지 확인하기
                Toast.makeText(this, "계정이 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PrivacyConditionActivity::class.java)
                intent.putExtra("name", viewBinding.etName.text.toString())
                intent.putExtra("email", viewBinding.etEmail.text.toString())
                intent.putExtra("phone", viewBinding.etPhone.text.toString())
                intent.putExtra("pw", viewBinding.etPw.text.toString())
                startActivity(intent)
            }
        }
    }
}