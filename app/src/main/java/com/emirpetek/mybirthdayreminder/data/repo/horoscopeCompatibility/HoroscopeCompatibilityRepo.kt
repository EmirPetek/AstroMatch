package com.emirpetek.mybirthdayreminder.data.repo.horoscopeCompatibility

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.network.OpenAIMessage
import com.emirpetek.mybirthdayreminder.network.OpenAIRequest
import com.emirpetek.mybirthdayreminder.network.OpenAIResponse
import com.emirpetek.mybirthdayreminder.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HoroscopeCompatibilityRepo {

    private val api = RetrofitInstance.api
    val data = MutableLiveData<String>()


    fun getAIResponse() : MutableLiveData<String>{
        return data
    }


    fun getOpenAIResponse(prompt: String, tokenNumber: Int) {

      /*  val request = OpenAIRequest(
            "gpt-4o-mini",
            prompt,
            1000,
        )*/

        val request = OpenAIRequest(
            model = "gpt-3.5-turbo",
            listOf(
                OpenAIMessage(role = "system", content = "You are a helpful assistant."),
                OpenAIMessage(role = "user", content = prompt)
            ),
            max_tokens = tokenNumber,
        )

        val call = api.getCompletion(request)
        call.enqueue(object : Callback<OpenAIResponse> {
            override fun onResponse(call: Call<OpenAIResponse>, response: Response<OpenAIResponse>) {
                if (response.isSuccessful) {
                    val aiResponse = response.body()
                    aiResponse?.choices?.get(0)?.message?.content.let {
                        data.value = it
                    }
                } else {
                    data.value = "Request failed with status: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
                Log.e("onFailure: ", t.message.toString())
                data.value = "Error: ${t.message}"
            }
        })
    }


}