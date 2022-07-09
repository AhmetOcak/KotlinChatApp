package com.ahmet.data.usecase

import com.ahmet.domain.interfaces.IFirebaseOperations
import com.ahmet.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseOperations : IFirebaseOperations {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(
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

    override suspend fun login(email: String, password: String): String? {
        var resultMessage: String? = null

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            resultMessage = if (it.isSuccessful) {
                "Login Successful"
            }else {
                it.exception!!.toString()
            }
        }.await()
        return resultMessage
    }

    override fun saveFirestore(
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

    override suspend fun readFirestore(collectionPath: String) {
        db.collection(collectionPath)
            .get()
            .addOnCompleteListener { }
    }
}