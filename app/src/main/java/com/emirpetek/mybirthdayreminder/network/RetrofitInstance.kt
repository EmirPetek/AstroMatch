package com.emirpetek.mybirthdayreminder.network

import com.aallam.openai.client.OpenAI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val apiKey = "sk-UQNHcQpPEX8-ATmbWcJfnjAL_PDxTdxaQYpGuGnt0oT3BlbkFJpV3LwHGGk-Fg6j5MP_bfcVFpMZMZavELQDB4WvmEoA"
    private val client =
        OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Bağlantı süresi aşımı
            .readTimeout(30, TimeUnit.SECONDS)     // Okuma süresi aşımı
            .writeTimeout(30, TimeUnit.SECONDS)    // Yazma süresi aşımı
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