package com.ahmet.data.repository

import com.ahmet.data.utils.Firebase
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IAuthRepository
import com.ahmet.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() : IAuthRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun reauthenticate(email: String, password: String): Boolean {
        var status = false
        val credential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser!!.reauthenticate(credential).addOnSuccessListener {
            status = true
        }.await()

        return status
    }

    override suspend fun register(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
        friendRequests: List<String>
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
                        friendRequests,
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
            }
        }.await()
        return resultMessage
    }

    override fun saveUserDoc(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
        friendRequests: List<String>,
        collectionPath: String
    ) {
        val user = User(userName, email, password, userFriends, friendRequests)
        val userData: MutableMap<String, Any> = HashMap()

        userData[UserKeys.USERNAME] = user.userName
        userData[UserKeys.EMAIL] = user.emailAddress
        userData[UserKeys.PASSWORD] = user.password
        userData[UserKeys.USER_FRIENDS] = user.userFriends
        userData[UserKeys.FRIEND_REQUESTS] = user.friendRequest

        db.collection(collectionPath).document(user.emailAddress).set(userData)
    }
}