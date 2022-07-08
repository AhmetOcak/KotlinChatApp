package com.ahmet.data.usecase

import com.ahmet.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseOperations {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun register(
        email: String,
        password: String,
        userName: String,
        collectionPath: String
    ): String? {
        var resultMessage: String? = null

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    resultMessage = "Register Successful"
                    saveFirestore(email, password, userName, collectionPath)
                } else {
                    resultMessage = it.exception!!.toString()
                }
            }.await()
        return resultMessage
    }

    private fun saveFirestore(
        email: String,
        password: String,
        userName: String,
        collectionPath: String
    ) {
        val user = User(userName, email, password)
        val userData: MutableMap<String, String> = HashMap()

        userData["username"] = user.userName
        userData["email"] = user.emailAddress
        userData["password"] = user.password

        db.collection(collectionPath)
            .document(user.emailAddress).set(userData)
    }

    suspend fun readFirestore(collectionPath: String) {
        db.collection(collectionPath)
            .get()
            .addOnCompleteListener { }
    }
}