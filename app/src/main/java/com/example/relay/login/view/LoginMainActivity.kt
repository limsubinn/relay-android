package com.example.relay.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.ui.MainActivity
import com.example.relay.databinding.ActivityLoginMainBinding
import com.example.relay.fcm.FireBaseClientService
import com.example.relay.fcm.FireBaseService
import com.example.relay.fcm.FirebaseInterface
import com.example.relay.fcm.data.UserDeviceTokenRes
import com.example.relay.login.service.LogInInterface
import com.example.relay.login.service.LogInService
import com.example.relay.login.models.LogInLocalRes
import com.google.firebase.messaging.FirebaseMessaging

class LoginMainActivity : AppCompatActivity(), LogInInterface, FirebaseInterface {
    private val viewBinding: ActivityLoginMainBinding by lazy{
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val prefsId = prefs.getString("id", "")
        val prefsPw = prefs.getString("pw", "")

        // 자동 로그인
        if (prefsId!!.isNotBlank() and prefsPw!!.isNotBlank())
            LogInService(this).tryPostLocalLogIn(prefsId, prefsPw)
        else
            Toast.makeText(this, "로컬 자동로그인 불가", Toast.LENGTH_SHORT).show()

        viewBinding.btnLogin.setOnClickListener {
            val id:String = viewBinding.etLoginId.text.toString()
            val pw:String = viewBinding.etLoginPw.text.toString()

            if (id.isBlank() || pw.isBlank()){
                Toast.makeText(this, "입력되지 않은 칸이 존재합니다.", Toast.LENGTH_SHORT).show()
            } else {
                LogInService(this).tryPostLocalLogIn(id, pw)
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

    override fun onPostLocalLogInSuccess(response: LogInLocalRes) {
        Toast.makeText(this@LoginMainActivity,"로컬 로그인 성공", Toast.LENGTH_SHORT).show()

        prefs.edit().putString("accessToken", response.result.accessToken).apply()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FireBaseClientService(this).tryPostUserDevice(it);
        }

        val intent = Intent(this@LoginMainActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPostLocalLogInTokenExpire(response: LogInLocalRes) {
        val editor = prefs.edit()
        editor.putString("accessToken", response.result.refreshToken).apply()
        LogInService(this)
            .tryPostLocalLogIn(viewBinding.etLoginId.text.toString(), viewBinding.etLoginPw.text.toString())
    }

    override fun onPostLocalLogInFailure(message: String) {
        Toast.makeText(this@LoginMainActivity,"로컬 로그인 실패", Toast.LENGTH_SHORT).show()
    }

    override fun onPostLocalLogInWrongPwd() {
        Toast.makeText(this@LoginMainActivity,"ID/PW를 확인하세요.", Toast.LENGTH_SHORT).show()
    }

    override fun onPostDeleteDeviceSuccess(response: UserDeviceTokenRes) {
        TODO("Not yet implemented")
    }
}