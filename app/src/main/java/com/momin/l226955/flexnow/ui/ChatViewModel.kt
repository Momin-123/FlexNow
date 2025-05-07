package com.momin.l226955.flexnow.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.momin.l226955.flexnow.ui.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ChatViewModel : ViewModel() {

    private val _responseText = MutableLiveData<String>()
    val responseText: LiveData<String> = _responseText

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenAIApi::class.java)

    fun sendMessageToChatGPT(userMessage: String) {
        val request = OpenAIRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message("user", userMessage))
        )

        api.askChatGPT(request).enqueue(object : Callback<OpenAIResponse> {
            override fun onResponse(
                call: Call<OpenAIResponse>,
                response: Response<OpenAIResponse>
            ) {
                if (response.isSuccessful) {
                    val reply = response.body()?.choices?.firstOrNull()?.message?.content
                    _responseText.postValue(reply ?: "No response")
                } else {
                    _responseText.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
                _responseText.postValue("Failure: ${t.message}")
            }
        })
    }
}