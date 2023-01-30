package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import com.example.relay.databinding.ActivityPrivacyConditionBinding

class PrivacyConditionActivity : AppCompatActivity() {
    private val viewBinding: ActivityPrivacyConditionBinding by lazy {
        ActivityPrivacyConditionBinding.inflate(layoutInflater)
    }

    private lateinit var getResultText: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // 회원가입 실패
        getResultText = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val mString = result.data?.getStringExtra("sign-up-fail")
                val intent = Intent()
                intent.putExtra("sign-up-fail", mString)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        viewBinding.btnAgree.setOnClickListener{
            val intent = Intent(this, LastCheckActivity::class.java)
            getResultText.launch(intent)
            // startActivity(intent)
        }
    }
}