package com.emirpetek.mybirthdayreminder.network

import com.aallam.openai.client.OpenAI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import java.util.concurrent.TimeUnit
import java.util.Properties
import java.io.FileInputStream

object RetrofitInstance {

    private val apiKey = getOpenAiApiKey().getApiKey()

    private val client =
        OkHttpClient
            .Builder()
            .connectTimeout(100, TimeUnit.SECONDS)  // Bağlantı süresi aşımı
            .readTimeout(100, TimeUnit.SECONDS)     // Okuma süresi aşımı
            .writeTimeout(100, TimeUnit.SECONDS)    // Yazma süresi aşımı
            .addInterceptor(Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        chain.proceed(request)
    }).build()


    val api: OpenAIApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAIApi::class.java)
    }
}