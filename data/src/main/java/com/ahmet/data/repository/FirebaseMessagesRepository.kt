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

@Suppress("ConvertToStringTemplate")
class FirebaseMessagesRepository @Inject constructor() : IFirebaseMessagesRepository {
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

            db.collection(Firebase.MESSAGES_COLLECTION_PATH).document(userEmail + " " + friendEmail)
                .set(data)
        }
    }

    override suspend fun listenPrivateMessages(
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
                    val userData =
                        value.data!!.getValue(EditEmail.removeDot(userEmail)) as MutableList<MutableMap<String, Any>>?
                    val friendData =
                        value.data!!.getValue(EditEmail.removeDot(friendEmail)) as MutableList<MutableMap<String, Any>>?
                    callback(Message(userData, friendData))
                } else {
                    val userData = mutableListOf<MutableMap<String, Any>>()
                    val friendData = mutableListOf<MutableMap<String, Any>>()
                    callback(Message(userData, friendData))
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
                    callback(value.documents)
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
                        db.collection(Firebase.MESSAGES_COLLECTION_PATH)
                            .document(friendEmail + " " + userEmail)
                    } else {
                        db.collection(Firebase.MESSAGES_COLLECTION_PATH)
                            .document(userEmail + " " + friendEmail)
                    }
                }.await()
        }

        return docId!!
    }

    private suspend fun receiveMessageFromNonFriend(userEmail: String): MutableList<String> {
        val userFriend: MutableList<String> = mutableListOf()

        db.collection(Firebase.MESSAGES_COLLECTION_PATH).get().addOnCompleteListener {
            for (doc in it.result.documents) {
                if (doc.id.findWord(userEmail)) {
                    if (doc.data?.get(doc.data?.keys!!.first()).toString() != "[]" && doc.data?.keys!!.first() != EditEmail.removeDot(userEmail)) {
                        userFriend.add(doc.data!!.keys.first())
                    }
                }
            }
        }.await()

        return userFriend
    }

    // if return "null" -> the user has already been added to friends or the user has not received any messages.
    // if return "{user email}" -> the user is not added in friends or the user has a message.
    suspend fun searchUserFriends(userEmail: String): MutableList<String> {
        val friendEmail = receiveMessageFromNonFriend(userEmail)
        val nonFriends: MutableList<String> = mutableListOf()
        var queryResult: Boolean

        for (i in 0 until friendEmail.size) {
            queryResult =
                FirebaseUserDataRepository().isUserAlreadyAdded(userEmail, friendEmail[i]) ?: true

            if (!queryResult) {
                nonFriends.add(EditEmail.addDot(friendEmail[i]))
            }
        }

        return nonFriends
    }

    private fun String.findWord(word: String) = "\\b$word\\b".toRegex().containsMatchIn(this)
}