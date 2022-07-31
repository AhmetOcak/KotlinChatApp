package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.FirebaseMessagesRepository
import com.ahmet.domain.model.Message
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class ListenAllMessageData @Inject constructor(private val repository: FirebaseMessagesRepository) {
    fun listenMessagesData(userEmail: String, callback: (messages: List<DocumentSnapshot>) -> Unit) =
        repository.listenAllMessages(userEmail, callback)
}