package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.FirebaseMessagesRepository
import javax.inject.Inject

class SendMessage @Inject constructor(private val repository: FirebaseMessagesRepository) {
    suspend fun sendMessage(message: String, userEmail: String, friendEmail: String, messageDate: Long) {
        repository.sendMessage(message, userEmail, friendEmail, messageDate)
    }
}