package com.ahmet.domain.interfaces

import com.google.firebase.firestore.DocumentSnapshot

interface IMessagesRepository {

    suspend fun createMessageDoc(userEmail: String, friendEmail: String)

    suspend fun listenPrivateMessages(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Any?) -> Unit
    )

    fun listenAllMessages(userEmail: String, callback: (messages: List<DocumentSnapshot>) -> Unit)

    suspend fun sendMessage(
        message: String,
        userEmail: String,
        friendEmail: String,
        messageDate: Long
    )

}