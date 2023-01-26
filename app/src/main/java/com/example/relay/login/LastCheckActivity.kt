package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.databinding.ActivityLastCheckBinding
import com.example.relay.login.data.SignUpLocalRes

class LastCheckActivity : AppCompatActivity(), SignUpInterface {
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
            val name: String? = prefs.getString("name", "")
            val email:String? = prefs.getString("email", "")
            val pw:String? = prefs.getString("pw", "")

            if ((name != null) && (email != null) && (pw != null)) {
                if (name.isEmpty() || email.isEmpty() || pw.isEmpty())
                    Toast.makeText(this@LastCheckActivity, "prefs 오류" , Toast.LENGTH_SHORT).show()
                else
                    SignUpService(this).tryPostLocalSignIn()

            }
        }
    }

    override fun onPostLocalSignUpInSuccess(response: SignUpLocalRes) {
        Toast.makeText(
            this@LastCheckActivity,
            "로컬 회원가입 성공",
            Toast.LENGTH_SHORT
        ).show()
        prefs.edit().putString("accessToken", response.result.accessToken).apply()
        val intent = Intent(
            this@LastCheckActivity,
            LoginMainActivity::class.java
        )
        finishAffinity()        // 스택에 쌓인 액티비티 비우기
        startActivity(intent)
    }

    override fun onPostLocalSignUpInFailure(message: String) {
        Toast.makeText(
            this@LastCheckActivity,
            "로컬 회원가입 실패",
            Toast.LENGTH_SHORT
        ).show()
    }
}