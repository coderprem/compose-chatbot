package com.example.chatbot

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constants.API_KEY,

    )

    fun sendMessage(question: String) {
        // Send message to chatBot
        Log.d("ChatViewModel", "Sending message: $question")

        viewModelScope.launch {

            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) {
                            text(it.message)
                        }
                    }.toList()
                )
                messageList.add(MessageModel(message = question, role = "user"))
                messageList.add(MessageModel(message = "Typing....", role = "model"))

                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(MessageModel(message = response.text.toString(), role = "model"))
            }
            catch (e: Exception) {
                messageList.removeLast()
                messageList.add(MessageModel(message = "Error: ${e.message}", role = "model"))
                Log.e("ChatViewModel", "Error: ${e.message}")
            }
        }
    }

}