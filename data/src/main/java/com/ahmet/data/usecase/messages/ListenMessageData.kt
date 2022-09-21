package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.MessagesRepository
import javax.inject.Inject

class ListenMessageData @Inject constructor(private val repository: MessagesRepository) {
    suspend fun listenPrivateData(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Any?) -> Unit
    ) {
        repository.listenPrivateMessages(userEmail, friendEmail, callback)
    }
}