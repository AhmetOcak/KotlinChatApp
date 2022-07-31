package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.FirebaseMessagesRepository
import com.ahmet.domain.model.Message
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ListenMessageData @Inject constructor(private val repository: FirebaseMessagesRepository) {
    suspend fun listenPrivateData(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Message?) -> Unit
    ) {
        repository.listenPrivateMessages(userEmail, friendEmail, callback)
    }
}