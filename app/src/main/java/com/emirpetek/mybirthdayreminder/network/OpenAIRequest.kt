package com.emirpetek.mybirthdayreminder.network

import com.aallam.openai.api.chat.ChatRole
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

     data class OpenAIRequest(val model: String, val messages: List<OpenAIMessage>, val max_tokens: Int)
    //data class OpenAIRequest(val model: String, val prompt: String, val max_tokens: Int)
