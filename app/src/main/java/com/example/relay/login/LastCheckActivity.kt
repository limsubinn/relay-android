package com.example.relay.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.relay.MyApplication
import com.example.relay.RetrofitClient

import com.example.relay.databinding.ActivityLastCheckBinding
import com.example.relay.login.data.BaseRes
import com.example.relay.login.data.SignInLocalReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LastCheckActivity : AppCompatActivity() {
    private val viewBinding: ActivityLastCheckBinding by lazy{
        ActivityLastCheckBinding.inflate(layoutInflater)
    }

    private val retrofit: Retrofit = RetrofitClient.getInstance() // RetrofitClient 의 instance 불러오기
    private val signupApi: LoginService = retrofit.create(LoginService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnBack.setOnClickListener{
            finish()
        }

        viewBinding.btnNext.setOnClickListener{
            val name:String = MyApplication.prefs.getString("name", "")
            val email:String = MyApplication.prefs.getString("email", "")
            val pw:String = MyApplication.prefs.getString("pw", "")

            if (name.isEmpty() || email.isEmpty() || pw.isEmpty())
                Toast.makeText(this@LastCheckActivity, "prefs 오류" , Toast.LENGTH_SHORT).show()
            else {
                Runnable {
                    signupApi.signInLocal(SignInLocalReq(name, email, pw))
                        .enqueue(object : Callback<BaseRes> {
                            // 전송 성공
                            override fun onResponse(
                                call: Call<BaseRes>,
                                response: Response<BaseRes>
                            ) {
                                if (response.isSuccessful) { // <--> response.code == 200
                                    // 성공 처리
                                    Log.d(
                                        "태그",
                                        "accessToken : ${response.body()?.result?.accessToken}"
                                    )
                                    Toast.makeText(
                                        this@LastCheckActivity,
                                        "로컬 로그인 성공",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    MyApplication.prefs.setString("accessToken", response.body()?.result!!.accessToken)
                                    val intent = Intent(
                                        this@LastCheckActivity,
                                        LoginMainActivity::class.java
                                    )
                                    finishAffinity()        // 스택에 쌓인 액티비티 비우기
                                    startActivity(intent)
                                } else {
                                    // 전송은 성공 but 4xx 에러
                                    Log.d("태그: 에러바디", "response : ${response.errorBody()}")
                                    Log.d("태그: 메시지", "response : ${response.message()}")
                                    Log.d("태그: 코드", "response : ${response.code()}")
                                    Toast.makeText(
                                        this@LastCheckActivity,
                                        "로컬 회원가입 실패",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            // 전송 실패
                            override fun onFailure(call: Call<BaseRes>, t: Throwable) {
                                Log.d("태그", t.message!!)
                            }
                        })
                }.run()
            }
        }
    }
}