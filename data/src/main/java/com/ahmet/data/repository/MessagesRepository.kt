package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.model.MessageEntity
import com.ahmet.data.utils.EditEmail
import com.ahmet.data.utils.Firebase
import com.ahmet.domain.interfaces.IMessagesRepository
import com.ahmet.domain.model.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("ConvertToStringTemplate")
class MessagesRepository @Inject constructor() : IMessagesRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun createMessageDoc(userEmail: String, friendEmail: String) {
        val isExist = isMessageDocCreated(userEmail, friendEmail)

        if (!isExist) {
            val message = Message(mutableListOf(), mutableListOf())
            val users = mutableListOf(userEmail, friendEmail)
            val data: MutableMap<String, Any> = HashMap()

            data[EditEmail.removeDot(userEmail)] = message.userMessage!!
            data[EditEmail.removeDot(friendEmail)] = message.friendMessage!!
            data["users"] = users

            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + " " + friendEmail).set(data)
        }
    }

    override suspend fun listenPrivateMessages(
        userEmail: String,
        friendEmail: String,
        callback: (messages: Any?) -> Unit
    ) {
        val messagesRef = setMessageDocRef(userEmail, friendEmail)

        messagesRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("SnapshotException", error.message.toString())
                return@addSnapshotListener
            }

            if (value != null) {
                if (!value.data.isNullOrEmpty()) {
                    val userData = value.data!!.getValue(EditEmail.removeDot(userEmail)) as MutableList<MutableMap<String, Any>>?
                    val friendData = value.data!!.getValue(EditEmail.removeDot(friendEmail)) as MutableList<MutableMap<String, Any>>?
                    callback(MessageEntity(userData, friendData))
                } else {
                    val userData = mutableListOf<MutableMap<String, Any>>()
                    val friendData = mutableListOf<MutableMap<String, Any>>()
                    callback(MessageEntity(userData, friendData))
                }
            }
        }
    }

    override fun listenAllMessages(userEmail: String, callback: (messages: List<DocumentSnapshot>) -> Unit) {
        db.collection(Firebase.MESSAGES_COLLECTION_PATH)
            .whereArrayContainsAny("users", mutableListOf(userEmail))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("listen all messages exception", error.toString())
                    return@addSnapshotListener
                }

                if (value != null) {
                    val data = value.documents.sortedByDescending { it.data?.keys.toString() }
                    callback(data)
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
        messagesRef.update(EditEmail.removeDot(userEmail), FieldValue.arrayUnion(messageData[0]))
    }

    private suspend fun isMessageDocCreated(userEmail: String, friendEmail: String): Boolean {
        var exist = false

        db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(friendEmail + " " + userEmail)
            .get()
            .addOnCompleteListener {
                exist = it.result.exists()
            }.await()

        return exist
    }

    private suspend fun setMessageDocRef(
        userEmail: String,
        friendEmail: String
    ): DocumentReference {
        var docId: DocumentReference? = null

        while (docId == null) {
            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(friendEmail + " " + userEmail)
                .get()
                .addOnCompleteListener {
                    docId = if (it.result.exists()) {
                        db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(friendEmail + " " + userEmail)
                    } else {
                        db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + " " + friendEmail)
                    }
                }.await()
        }

        return docId!!
    }
}