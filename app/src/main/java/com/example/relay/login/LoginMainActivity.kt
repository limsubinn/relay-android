package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.example.relay.MainActivity
import com.example.relay.RetrofitClient
import com.example.relay.databinding.ActivityLoginMainBinding
import com.example.relay.login.data.localLogInData
import com.example.relay.login.response.localLogInRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginMainActivity : AppCompatActivity() {

    private val viewBinding: ActivityLoginMainBinding by lazy{
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    private val retrofit: Retrofit = RetrofitClient.getInstance() // RetrofitClient 의 instance 불러오기
    private val loginApi: loginService = retrofit.create(loginService::class.java) // retrofit 이 interface 구현

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val id = viewBinding.etLoginId.text.toString()
        val pw = viewBinding.etLoginPw.text.toString()

        viewBinding.btnLogin.setOnClickListener {
            if (id.isBlank() || pw.isBlank()){
                Toast.makeText(this, "입력되지 않은 칸이 존재합니다.", Toast.LENGTH_SHORT).show()
            } else {
                Runnable {
                    loginApi.logInLocal(localLogInData(id, pw)).enqueue(object : Callback<localLogInRes>{
                        // 전송 성공
                        override fun onResponse(call: Call<localLogInRes>, response: Response<localLogInRes>) {
                            if(response.isSuccessful) { // <--> response.code == 200
                                // 성공 처리
                                Log.d("태그", "response : ${response.body()?.code}")
                                Toast.makeText(this@LoginMainActivity, "로컬 로그인 성공" , Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginMainActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // 전송은 성공 but 4xx 에러
                                Log.d("태그: 에러바디", "response : ${response.errorBody()}")
                                Log.d("태그: 메시지", "response : ${response.message()}")
                                Log.d("태그: 코드", "response : ${response.code()}")
                                Toast.makeText(this@LoginMainActivity, "로컬 로그인 실패" , Toast.LENGTH_SHORT).show()
                            }
                        }
                        // 전송 실패
                        override fun onFailure(call: Call<localLogInRes>, t: Throwable) {
                            Log.d("태그", t.message!!)
                        }
                    })
                }.run()
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
}