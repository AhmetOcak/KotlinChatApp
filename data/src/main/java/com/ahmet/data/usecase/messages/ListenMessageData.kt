package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.FirebaseMessagesRepository
import javax.inject.Inject

class ListenMessageData @Inject constructor(private val repository: FirebaseMessagesRepository) {
    suspend fun listenPrivateData(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Any?) -> Unit
    ) {
        repository.listenPrivateMessages(userEmail, friendEmail, callback)
    }
}