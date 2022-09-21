package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.utils.EditEmail
import com.ahmet.data.utils.Firebase
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IFriendRepository
import com.ahmet.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendRepository @Inject constructor() : IFriendRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun sendFriendRequest(userEmail: String, friendEmail: String): String? {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(friendEmail)
        val isUserAlreadyAdded = isUserAlreadyAdded(userEmail, friendEmail)
        var resultMessage: String? = null

        if (isUserExist(friendEmail) == true) {
            if (isUserAlreadyAdded == true) {
                resultMessage = "You already added this account"
            } else {
                userRef.update("friend_requests", FieldValue.arrayUnion(userEmail))
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            resultMessage = "Successful"
                        }else {
                            resultMessage = "Unsuccessful"
                        }
                    }.await()
            }
        } else {
            resultMessage = "There is no such account"
        }

        return resultMessage
    }

    override suspend fun getFriendRequests(email: String): List<String> {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(email)
        var friendRequests: List<String> = listOf()

        userRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("result", "successful")
                friendRequests = it.result.data?.get(UserKeys.FRIEND_REQUESTS) as List<String>
            } else {
                Log.e("result", "fail")
            }
        }.await()

        return friendRequests
    }

    override suspend fun addUser(friendEmail: String, userEmail: String): String? {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(userEmail)
        var resultMessage: String? = null

        userRef.update("user_friends", FieldValue.arrayUnion(friendEmail))
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    resultMessage = "Successful"
                }else {
                    resultMessage = "Unsuccessful"
                }
            }.await()

        return resultMessage
    }

    override suspend fun deleteFriendRequest(friendEmail: String, userEmail: String): String? {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(userEmail)
        var resultMessage: String? = null

        userRef.update("friend_requests", FieldValue.arrayRemove(friendEmail))
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    resultMessage = "Successful"
                }else {
                    resultMessage = "Unsuccessful"
                }
            }.await()

        return resultMessage
    }

    override suspend fun getUserDoc(email: String): User? {
        val userDoc = db.collection(Firebase.USER_COLLECTION_PATH).document(email)
        var user: User? = null

        userDoc.get().addOnSuccessListener {
            if (!it.data.isNullOrEmpty()) {
                val userFriendsList = it.get(UserKeys.USER_FRIENDS) as List<String>
                val friendRequests = it.get(UserKeys.FRIEND_REQUESTS) as List<String>
                user = User(
                    it.data!![UserKeys.USERNAME].toString(),
                    it.data!![UserKeys.EMAIL].toString(),
                    it.data!![UserKeys.PASSWORD].toString(),
                    userFriendsList,
                    friendRequests
                )
            }
        }.await()
        return user
    }

    // created for sendFriendRequest function
    private suspend fun isUserAlreadyAdded(userEmail: String, friendEmail: String): Boolean? {
        var queryResult: Boolean? = null
        val user = getUserDoc(userEmail)

        if (user != null) {
            if (user.userFriends.isNullOrEmpty()) {
                queryResult = false
            } else {
                for (friend in user.userFriends) {
                    if (EditEmail.removeDot(friend) == EditEmail.removeDot(friendEmail)) {
                        queryResult = true
                        break
                    }
                    queryResult = false
                }
            }
        } else {
            queryResult = true
        }

        return queryResult
    }

    // created for sendFriendRequest function
    private suspend fun isUserExist(friendEmail: String): Boolean? {
        val friendRef = db.collection(Firebase.USER_COLLECTION_PATH).document(friendEmail)
        var exist: Boolean? = null

        friendRef.get().addOnSuccessListener { document ->
            exist = document.exists()
        }.await()

        return exist
    }


}