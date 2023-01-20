package com.example.relay

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

// 싱글톤 패턴
// 하나의 Retrofit 객체만를 생성하여 재사용
object RetrofitClient {
    private var instance: Retrofit?= null
    private const val CONNECT_TIMEOUT_SEC = 20000L
    private const val BASE_URL = "http://3.38.32.124/"

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", MyApplication.prefs.getString("accessToken", ""))
                .build()
            proceed(newRequest)
        }
    }

    fun okHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor) // okHttp 에 인터셉터 추가
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    fun getInstance() : Retrofit {
        if(instance == null){
            // 로깅인터셉터 세팅
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            // OKHttpClient에 로깅인터셉터 등록
            //val client = OkHttpClient.Builder()
            //    .addInterceptor(interceptor)
            //    .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            //    .build()

            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient(AppInterceptor())) // Retrofit 객체에 OkHttpClient 적용
                .build()
        }
        return instance!!
    }
}