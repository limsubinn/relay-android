package com.example.relay.login

import android.util.Log
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.ApplicationClass.Companion.sRetrofit
import com.example.relay.login.data.LogInLocalReq
import com.example.relay.login.data.LogInLocalRes
import com.example.relay.login.data.SignUpLocalReq
import com.example.relay.login.data.SignUpLocalRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInService(val logInInterface: LogInInterface) {
    private val retrofit: LogInRetrofit = sRetrofit.create(LogInRetrofit::class.java)

    fun tryPostLocalLogIn(id:String, pw:String){
        val accessToken = prefs.getString("accessToken", "")

        Log.d("LogInLocalPrefs","accessToken: $accessToken")

        if (id!!.isBlank() || pw!!.isBlank())
            Log.d("LoginLocal","prefs error")
        else {
            retrofit.postLogInLocalReq(LogInLocalReq(id, pw)).enqueue((object : Callback<LogInLocalRes> {
                // 전송 성공
                override fun onResponse(call: Call<LogInLocalRes>, response: Response<LogInLocalRes>) {
                    Log.d("LogInLocal","accessToken : ${response.body()?.result?.accessToken}")
                    Log.d("LogInLocal","refreshToken : ${response.body()?.result?.refreshToken}")
                    if (response.isSuccessful) { // <--> response.code == 200
                        // 성공 처리
                        if (response.body()?.isSuccess == true) {
                            val editor = prefs.edit()
                            editor.putString("id", id).apply()
                            editor.putString("pw", pw).apply()
                            logInInterface.onPostLocalLogInSuccess(response.body() as LogInLocalRes)
                        } else {
                            //if (response.body()?.result?.refreshToken != null)
                            //    logInInterface.onPostLocalLogInTokenExpire(response.body() as LogInLocalRes)
                            val code = response.body()?.code
                            if (code == 2006 || code == 2007)
                                logInInterface.onPostLocalLogInWrongPwd()
                            Log.d("LogInLocal", "unknown error")
                        }
                    } else {
                        // 전송은 성공 but 4xx 에러
                        Log.d("LogInLocal", "failure")
                    }
                }

                // 전송 실패
                override fun onFailure(call: Call<LogInLocalRes>, t: Throwable) {
                    Log.d("태그", t.message!!)
                    t.printStackTrace()
                    logInInterface.onPostLocalLogInFailure(t.message ?: "통신 오류")
                }
            }))
        }
    }
}

class SignUpService(val signUpInterface: SignUpInterface) {
    private val retrofit: LogInRetrofit = sRetrofit.create(LogInRetrofit::class.java)

    fun tryPostLocalSignIn(){
        val name = prefs.getString("name", "")
        val email = prefs.getString("email", "")
        val pw = prefs.getString("pw", "")

        if (name!!.isBlank() || email!!.isBlank() || pw!!.isBlank())
            Log.d("SignUpLocal","prefs error")
        else {
            retrofit.postSignUpLocalReq(SignUpLocalReq(name, email, pw)).enqueue((object : Callback<SignUpLocalRes>{
                // 전송 성공
                override fun onResponse(call: Call<SignUpLocalRes>, response: Response<SignUpLocalRes>) {
                    if (response.isSuccessful) { // <--> response.code == 200
                        // 성공 처리
                        Log.d("SignUpLocal","accessToken : ${response.body()?.result?.accessToken}")
                        Log.d("SignUpLocal","refreshToken : ${response.body()?.result?.refreshToken}")
                        signUpInterface.onPostLocalSignUpInSuccess(response.body() as SignUpLocalRes)
                    } else {
                        // 전송은 성공 but 4xx 에러
                        Log.d("SignUpLocal", "failure")
                    }
                }

                // 전송 실패
                override fun onFailure(call: Call<SignUpLocalRes>, t: Throwable) {
                    Log.d("태그", t.message!!)
                    t.printStackTrace()
                    signUpInterface.onPostLocalSignUpInFailure(t.message ?: "통신 오류")
                }
            }))
        }
    }
}