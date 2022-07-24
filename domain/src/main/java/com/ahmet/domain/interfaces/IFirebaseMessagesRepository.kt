package com.ahmet.domain.interfaces

import com.ahmet.domain.model.Message

interface IFirebaseMessagesRepository {

    suspend fun createMessageDoc(userEmail: String, friendEmail: String)

    fun listenMessages(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Message?) -> Unit
    )
}