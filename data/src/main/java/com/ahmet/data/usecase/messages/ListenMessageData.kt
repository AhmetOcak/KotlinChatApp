package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.FirebaseMessagesRepository
import com.ahmet.domain.model.Message
import javax.inject.Inject

class ListenMessageData @Inject constructor(private val repository: FirebaseMessagesRepository) {
    fun listenData(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Message?) -> Unit
    ) {
        repository.listenMessages(userEmail, friendEmail, callback)
    }
}