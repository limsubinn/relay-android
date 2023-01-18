package com.example.relay.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

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
            val num1 = viewBinding.etNum1.text.toString()
            val num2 = viewBinding.etNum2.text.toString()
            val num3 = viewBinding.etNum3.text.toString()
            val num4 = viewBinding.etNum4.text.toString()

            //Log.d("toString", "auth num = $num1$num2$num3$num4")
        }
    }
}