package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IFirebaseUserDataRepository
import com.ahmet.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataRepository @Inject constructor(): IFirebaseUserDataRepository {

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
                    saveUserDoc(email, password, userName, userFriends, "users")
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
                user = User(
                    it.data!![UserKeys.USERNAME].toString(),
                    it.data!![UserKeys.EMAIL].toString(),
                    it.data!![UserKeys.PASSWORD].toString(),
                    listOf(it.data!![UserKeys.USER_FRIENDS].toString())
                )
            }
        }.await()
        return user
    }
}