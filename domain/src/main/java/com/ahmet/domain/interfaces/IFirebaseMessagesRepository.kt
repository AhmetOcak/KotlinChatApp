package com.ahmet.domain.interfaces

import com.google.firebase.firestore.DocumentSnapshot

interface IFirebaseMessagesRepository {

    suspend fun createMessageDoc(userEmail: String, friendEmail: String)

    suspend fun listenPrivateMessages(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Any?) -> Unit
    )

    suspend fun sendMessage(
        message: String,
        userEmail: String,
        friendEmail: String,
        messageDate: Long
    )

    fun listenAllMessages(userEmail: String, callback: (messages: List<DocumentSnapshot>) -> Unit)

    suspend fun searchUserFriends(userEmail: String): MutableList<String>

}