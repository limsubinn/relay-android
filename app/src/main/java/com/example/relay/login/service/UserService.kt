package com.example.relay.login.service

import android.content.Context
import android.util.Log
import com.example.relay.ApplicationClass.Companion.prefs
import com.example.relay.ApplicationClass.Companion.sRetrofit
import com.example.relay.login.models.*
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
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

class LogInSnsService(val snsInterface: LogInSNSInterface) {
    fun tryKakaoLogIn(context:Context){
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("SignUpKakao", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
                } else if (token != null) {
                    Log.i("SignUpKakao", "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
        }
    }

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("LoginKakao", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("LoginKakao", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    fun tryGoogleLogIn(){

    }

    fun tryNaverLogIn(){

    }
}

class SignUpService(val signUpInterface: SignUpInterface) {
    private val retrofit: LogInRetrofit = sRetrofit.create(LogInRetrofit::class.java)

    fun tryPostLocalSignUp(){
        val name = prefs.getString("name", "")
        val email = prefs.getString("email", "")
        val pw = prefs.getString("pw", "")

        if (name!!.isBlank() || email!!.isBlank() || pw!!.isBlank())
            Log.d("SignUpLocal","prefs error")
        else {
            retrofit.postSignUpLocalReq(SignUpLocalReq(name, email, pw)).enqueue((object : Callback<SignUpLocalRes>{
                override fun onResponse(call: Call<SignUpLocalRes>, response: Response<SignUpLocalRes>) {
                    if (response.code() == 200) {
                        Log.d("SignUpLocal","accessToken : ${response.body()?.result?.accessToken}")
                        Log.d("SignUpLocal","refreshToken : ${response.body()?.result?.refreshToken}")
                        signUpInterface.onPostLocalSignUpInSuccess(response.body() as SignUpLocalRes)
                    } else {
                        Log.d("SignUpLocal", "failure")
                    }
                }
                override fun onFailure(call: Call<SignUpLocalRes>, t: Throwable) {
                    Log.d("태그", t.message!!)
                    t.printStackTrace()
                    signUpInterface.onPostLocalSignUpInFailure(t.message ?: "통신 오류")
                }
            }))
        }
    }

    // 프로필 신규 생성
//    fun tryPostUserProfile(name: String, imgUrl: String, isAlarmOn: String, nickname: String, statusMsg: String){
//        retrofit.postUserProfileReq(name, UserProfileReq(imgUrl, isAlarmOn, nickname, statusMsg)).enqueue((object : Callback<UserProfileRes>{
//            // 전송 성공
//            override fun onResponse(call: Call<UserProfileRes>, response: Response<UserProfileRes>) {
//                if (response.isSuccessful) { // <--> response.code == 200
//                    // 성공 처리
//                    Log.d("UserProfile","success")
//                    signUpInterface.onPostUserProfileSuccess(response.body() as UserProfileRes)
//                } else {
//                    // 전송은 성공 but 4xx 에러
//                    Log.d("UserProfile", "failure")
//                }
//            }
//
//            // 전송 실패
//            override fun onFailure(call: Call<UserProfileRes>, t: Throwable) {
//                Log.d("태그", t.message!!)
//                t.printStackTrace()
//                signUpInterface.onPostUserProfileFailure(t.message ?: "통신 오류")
//            }
//        }))
//    }

}