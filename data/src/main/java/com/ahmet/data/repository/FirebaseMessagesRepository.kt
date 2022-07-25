package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.utils.EditEmail
import com.ahmet.data.utils.Firebase
import com.ahmet.domain.interfaces.IFirebaseMessagesRepository
import com.ahmet.domain.model.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMessagesRepository @Inject constructor() : IFirebaseMessagesRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun createMessageDoc(userEmail: String, friendEmail: String) {
        val isExist = isMessageDocCreated(userEmail, friendEmail)

        if (!isExist) {
            val message = Message(listOf(), listOf())
            val data: MutableMap<String, Any> = HashMap()

            data[EditEmail.edit(userEmail)] = message.userMessage!!
            data[EditEmail.edit(friendEmail)] = message.friendMessage!!

            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + friendEmail)
                .set(data)
        }
    }

    override suspend fun listenMessages(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Message?) -> Unit
    ) {
        val messagesRef = setMessageDocRef(userEmail, friendEmail)

        messagesRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("SnapshotException", error.message.toString())
                return@addSnapshotListener
            }

            if (value != null) {
                if (!value.data.isNullOrEmpty()) {
                    val userData = value.data!!.getValue(EditEmail.edit(userEmail)) as List<Map<String, Any>>?
                    val friendData = value.data!!.getValue(EditEmail.edit(friendEmail)) as List<Map<String, Any>>?
                    callback(Message(userData, friendData))
                } else {
                    val userData = listOf<Map<String, Any>>()
                    val friendData = listOf<Map<String, Any>>()
                    callback(Message(userData, friendData))
                }
            }
        }
    }

    override suspend fun sendMessage(
        message: String,
        userEmail: String,
        friendEmail: String,
        messageDate: Long
    ) {
        val messagesRef = setMessageDocRef(userEmail, friendEmail)
        val messageData: List<Map<String, Any>> = listOf(
            hashMapOf(
                "message" to message,
                "date" to Timestamp(messageDate, 15000)
            )
        )
        messagesRef.update(EditEmail.edit(userEmail), FieldValue.arrayUnion(messageData[0]))
    }

    private suspend fun isMessageDocCreated(userEmail: String, friendEmail: String): Boolean {
        var exist = false

        db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(friendEmail + userEmail).get()
            .addOnCompleteListener {
                exist = it.result.exists()
            }.await()

        return exist
    }

    private suspend fun setMessageDocRef(
        userEmail: String,
        friendEmail: String
    ): DocumentReference {
        return if (!isMessageDocCreated(userEmail, friendEmail)) {
            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + friendEmail)
        } else {
            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(friendEmail + userEmail)
        }
    }
}