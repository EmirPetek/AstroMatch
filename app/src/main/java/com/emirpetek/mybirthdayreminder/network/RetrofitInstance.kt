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

   // private val apiKey = "sk-UQNHcQpPEX8-ATmbWcJfnjAL_PDxTdxaQYpGuGnt0oT3BlbkFJpV3LwHGGk-Fg6j5MP_bfcVFpMZMZavELQDB4WvmEoA"
    private val apiKey = getOpenAiApiKey().getApiKey()//"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"
    //private val apiKey = BuildConfig.userCompatibilityApiKey

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