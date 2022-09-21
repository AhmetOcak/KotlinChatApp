package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.MessagesRepository
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class ListenAllMessageData @Inject constructor(private val repository: MessagesRepository) {
    fun listenMessagesData(userEmail: String, callback: (messages: List<DocumentSnapshot>) -> Unit) =
        repository.listenAllMessages(userEmail, callback)
}