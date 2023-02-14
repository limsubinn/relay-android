package com.example.relay.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.relay.databinding.ActivityForgetPwBinding
import com.example.relay.login.service.ConfirmCodeInterface
import com.example.relay.login.service.ConfirmCodeService

class ForgetPwActivity : AppCompatActivity(), ConfirmCodeInterface {
    private val binding: ActivityForgetPwBinding by lazy{
        ActivityForgetPwBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener{
            val email = binding.etPhoneEmail.text.toString()
            if (email.isBlank())
                Toast.makeText(this, "입력되지 않은 칸이 존재합니다.", Toast.LENGTH_SHORT).show()
            else {
                ConfirmCodeService(this).tryPostConfirmCodeEmail(email)
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onPostCodeByEmailSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPostCodeByEmailFailure() {
        TODO("Not yet implemented")
    }
}