package com.example.relay

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// 싱글톤 패턴
// 하나의 Retrofit 객체만를 생성하여 재사용
object RetrofitClient {
    private var instance: Retrofit?= null
    private val CONNECT_TIMEOUT_SEC = 20000L

    fun getInstance() : Retrofit {
        if(instance == null){

            // 로깅인터셉터 세팅
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            // OKHttpClient에 로깅인터셉터 등록
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build()

            instance = Retrofit.Builder()
                .baseUrl("http://3.38.32.124/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) // Retrofit 객체에 OkHttpClient 적용
                .build()
        }
        return instance!!
    }
}