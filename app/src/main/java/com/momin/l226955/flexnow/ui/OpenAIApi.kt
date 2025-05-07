package com.momin.l226955.flexnow.ui

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class OpenAIRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

interface OpenAIApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer AIzaSyCrz3nDvgcJh_8xaZefWp7omeBNiW-qttA"
    )
    @POST("v1/chat/completions")
    fun askChatGPT(@Body request: OpenAIRequest): Call<OpenAIResponse>
}