package com.momin.l226955.flexnow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.momin.l226955.flexnow.R
import com.momin.l226955.flexnow.ui.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ChatbotFragment : Fragment() {

    private lateinit var inputMessage: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)

        inputMessage = view.findViewById(R.id.inputMessage)
        sendButton = view.findViewById(R.id.sendButton)
        messagesContainer = view.findViewById(R.id.messagesContainer)

        sendButton.setOnClickListener {
            val message = inputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                showMessage("You: $message")
                inputMessage.setText("")
                askOpenAI(message)
            }
        }

        return view
    }

    private fun showMessage(msg: String) {
        val textView = TextView(requireContext())
        textView.text = msg
        textView.setPadding(8, 8, 8, 8)
        messagesContainer.addView(textView)
    }

    private fun askOpenAI(question: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OpenAIApi::class.java)

        val request = OpenAIRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message("user", question))
        )

        api.askChatGPT(request).enqueue(object : Callback<OpenAIResponse> {
            override fun onResponse(call: Call<OpenAIResponse>, response: Response<OpenAIResponse>) {
                val answer = if (response.isSuccessful) {
                    response.body()?.choices?.firstOrNull()?.message?.content ?: "No response"
                } else {
                    "Error: ${response.code()}"
                }
                showMessage("Bot: $answer")
            }

            override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
                showMessage("Error: ${t.message}")
            }
        })
    }
}