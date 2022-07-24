package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.utils.Firebase
import com.ahmet.domain.interfaces.IFirebaseMessagesRepository
import com.ahmet.domain.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import javax.inject.Inject

class FirebaseMessagesRepository @Inject constructor() : IFirebaseMessagesRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun createMessageDoc(userEmail: String, friendEmail: String) {
        val message = Message(listOf(), listOf())
        val data: MutableMap<String, Any> = HashMap()

        data[userEmail] = message.userMessage
        data[friendEmail] = message.friendMessage

        db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + friendEmail).set(data)
    }

    override fun listenMessages(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Message?) -> Unit
    ) {
        val messagesRef =
            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + friendEmail)

        messagesRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("SnapshotException", error.message.toString())
                return@addSnapshotListener
            }

            if (value != null) {
                val userData = value.data!![userEmail] as List<Map<String, Any>>
                val friendData = value.data!![friendEmail] as List<Map<String, Any>>
                callback(Message(userData, friendData))
            }
        }
    }
}