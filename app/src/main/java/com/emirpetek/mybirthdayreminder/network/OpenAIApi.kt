package com.emirpetek.mybirthdayreminder.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun getCompletion(@Body request: OpenAIRequest): Call<OpenAIResponse>
}