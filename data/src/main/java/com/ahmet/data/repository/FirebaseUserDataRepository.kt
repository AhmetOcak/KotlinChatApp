package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.utils.Firebase
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IFirebaseUserDataRepository
import com.ahmet.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataRepository @Inject constructor() : IFirebaseUserDataRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>
    ): String? {
        var resultMessage: String? = null

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    resultMessage = "Register Successful"
                    saveUserDoc(
                        email,
                        password,
                        userName,
                        userFriends,
                        Firebase.USER_COLLECTION_PATH
                    )
                } else {
                    resultMessage = it.exception!!.toString()
                }
            }.await()
        return resultMessage
    }

    override suspend fun login(email: String, password: String): String? {
        var resultMessage: String? = null

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                resultMessage = "Login Successful"
            } else {
                resultMessage = it.exception!!.message
                Log.e("e", resultMessage.toString())
            }
        }.await()
        return resultMessage
    }

    override fun saveUserDoc(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
        collectionPath: String
    ) {
        val user = User(userName, email, password, userFriends)
        val userData: MutableMap<String, Any> = HashMap()

        userData[UserKeys.USERNAME] = user.userName
        userData[UserKeys.EMAIL] = user.emailAddress
        userData[UserKeys.PASSWORD] = user.password
        userData[UserKeys.USER_FRIENDS] = user.userFriends

        db.collection(collectionPath)
            .document(user.emailAddress).set(userData)
    }

    override suspend fun getUserDoc(email: String): User? {
        val userDoc = db.collection("users").document(email)
        var user: User? = null

        userDoc.get().addOnSuccessListener {
            if (!it.data.isNullOrEmpty()) {
                val userFriendsList = it.get("user_friends") as List<String>
                user = User(
                    it.data!![UserKeys.USERNAME].toString(),
                    it.data!![UserKeys.EMAIL].toString(),
                    it.data!![UserKeys.PASSWORD].toString(),
                    userFriendsList
                )
            }
        }.await()
        return user
    }

    override suspend fun addUser(friendEmail: String, userEmail: String): String? {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(userEmail)
        var resultMessage: String? = null

        val data: MutableMap<String, List<String>> = HashMap()
        data[UserKeys.USER_FRIENDS] = listOf(friendEmail)

        db.collection(Firebase.USER_COLLECTION_PATH)
            .whereArrayContains("user_friends", friendEmail)
            .get()
            .addOnSuccessListener {
                if(it.documents.isEmpty()) {
                    userRef.update("user_friends", FieldValue.arrayUnion(friendEmail))
                        .addOnSuccessListener { resultMessage = "Successful" }
                        .addOnFailureListener { resultMessage = "Unsuccessful" }
                }else {
                    resultMessage = "You already added this account"
                }
            }.await()
        
        return resultMessage
    }
}