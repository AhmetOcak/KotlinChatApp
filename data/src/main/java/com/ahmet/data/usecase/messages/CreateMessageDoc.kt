package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.MessagesRepository
import javax.inject.Inject

class CreateMessageDoc @Inject constructor(private val repository: MessagesRepository) {
    suspend fun createMessageDoc(userEmail: String, friendEmail: String) {
        repository.createMessageDoc(userEmail, friendEmail)
    }
}