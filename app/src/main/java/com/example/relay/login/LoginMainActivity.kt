package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.MainActivity
import com.example.relay.databinding.ActivityLoginMainBinding
import com.example.relay.login.data.LogInLocalRes

class LoginMainActivity : AppCompatActivity(), LogInInterface {
    private val viewBinding: ActivityLoginMainBinding by lazy{
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnLogin.setOnClickListener {
            val id:String = viewBinding.etLoginId.text.toString()
            val pw:String = viewBinding.etLoginPw.text.toString()

            if (id.isBlank() || pw.isBlank()){
                Toast.makeText(this, "입력되지 않은 칸이 존재합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val editor = prefs.edit()
                editor.putString("id", id).apply()
                editor.putString("pw", pw).apply()
                LogInService(this).tryPostLocalLogIn()
            }
        }

        viewBinding.btnFindPw.setOnClickListener {
            val intent = Intent(this, ForgetPwActivity::class.java)
            startActivity(intent)
        }

        viewBinding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPostLocalLogInSuccess(res: LogInLocalRes) {
        Toast.makeText(this@LoginMainActivity,"로컬 로그인 성공", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@LoginMainActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPostLocalLogInTokenExpire(response: LogInLocalRes) {
        val editor = prefs.edit()
        editor.putString("accessToken", response.result.refreshToken).apply()
        LogInService(this).tryPostLocalLogIn()
    }

    override fun onPostLocalLogInFailure(message: String) {
        Toast.makeText(this@LoginMainActivity,"로컬 로그인 실패", Toast.LENGTH_SHORT).show()
    }

    override fun onPostLocalLogInWrongPwd() {
        Toast.makeText(this@LoginMainActivity,"ID/PW를 확인하세요.", Toast.LENGTH_SHORT).show()
    }
}