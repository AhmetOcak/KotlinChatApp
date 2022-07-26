package com.ahmet.data.repository

import android.util.Log
import com.ahmet.data.utils.EditEmail
import com.ahmet.data.utils.Firebase
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IFirebaseUserDataRepository
import com.ahmet.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
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

        val isUserAlreadyAdded = isUserAlreadyAdded(userEmail, friendEmail)

        if (isUserExist(friendEmail) == true) {
            db.collection(Firebase.USER_COLLECTION_PATH)
                .whereArrayContains("user_friends", friendEmail)
                .get()
                .addOnSuccessListener {
                    if (isUserAlreadyAdded == true) {
                        resultMessage = "You already added this account"
                    } else {
                        userRef.update("user_friends", FieldValue.arrayUnion(friendEmail))
                            .addOnSuccessListener { resultMessage = "Successful" }
                            .addOnFailureListener { resultMessage = "Unsuccessful" }
                    }
                }.await()
        } else {
            resultMessage = "There is no such account"
        }

        return resultMessage
    }

    // created for addUser function
    private suspend fun isUserExist(friendEmail: String): Boolean? {
        val friendRef = db.collection(Firebase.USER_COLLECTION_PATH).document(friendEmail)
        var exist: Boolean? = null

        friendRef.get().addOnSuccessListener { document ->
            exist = document.exists()
        }.await()

        return exist
    }

    // created for addUser function
    suspend fun isUserAlreadyAdded(userEmail: String, friendEmail: String): Boolean? {
        var queryResult: Boolean? = null
        val user = getUserDoc(userEmail)

        if (user != null) {
            if (user.userFriends.isNullOrEmpty()) {
                queryResult = false
            } else {
                for (friend in user.userFriends) {
                    if (EditEmail.edit(friend) == EditEmail.edit(friendEmail)) {
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

    override suspend fun deleteUser(): String? {
        var result: String? = null

        auth.currentUser!!.delete().addOnSuccessListener {
            result = "Successful"
        }.await()

        return result
    }

    override suspend fun deleteUserDoc(email: String): Boolean {
        var status = false

        db.collection(Firebase.USER_COLLECTION_PATH).document(email).delete()
            .addOnSuccessListener {
                status = true
            }.await()

        return status
    }

    override suspend fun reauthenticate(email: String, password: String): Boolean {
        var status = false
        val credential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser!!.reauthenticate(credential).addOnSuccessListener {
            status = true
        }.await()

        return status
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}
