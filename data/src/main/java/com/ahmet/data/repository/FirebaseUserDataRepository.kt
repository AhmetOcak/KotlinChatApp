package com.ahmet.data.repository

import android.net.Uri
import android.util.Log
import com.ahmet.data.utils.EditEmail
import com.ahmet.data.utils.Firebase
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IFirebaseUserDataRepository
import com.ahmet.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class FirebaseUserDataRepository @Inject constructor() : IFirebaseUserDataRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getCurrentUserEmail(): String? = auth.currentUser?.email

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

    // created for sendFriendRequest function
    private suspend fun isUserExist(friendEmail: String): Boolean? {
        val friendRef = db.collection(Firebase.USER_COLLECTION_PATH).document(friendEmail)
        var exist: Boolean? = null

        friendRef.get().addOnSuccessListener { document ->
            exist = document.exists()
        }.await()

        return exist
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

    override suspend fun deleteUser(): String? {
        var result: String? = null

        auth.currentUser!!.delete().addOnSuccessListener {
            result = "Successful"
        }.await()

        return result
    }

    override suspend fun deleteUserDoc(email: String): Boolean {
        var status = false

        db.collection(Firebase.USER_COLLECTION_PATH).document(email).delete().addOnSuccessListener {
            status = true
        }.await()

        return status
    }

    override suspend fun uploadImage(filePath: Uri): String? {
        var resultMessage: String? = null
        val imagesRef = FirebaseStorage.getInstance().reference.child(("${auth.currentUser?.email}/${auth.currentUser?.email}.png"))

        imagesRef.putFile(filePath).addOnCompleteListener {
            resultMessage = if (it.isSuccessful) {
                "Successful"
            } else {
                it.exception?.message
            }
        }.await()

        return resultMessage
    }

    override suspend fun getUserImage(email: String): String? {
        val imagesRef = FirebaseStorage.getInstance().reference.child(("${email}/${email}.png"))
        val localFile = File.createTempFile("userImage", "png")

        imagesRef.getFile(localFile).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("result", "successful")
            } else {
                Log.e("result", "fail")
            }
        }.await()

        return localFile.path
    }

    override suspend fun updateUserName(email: String, newUserName: String) {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(email)

        userRef.update(UserKeys.USERNAME, newUserName).addOnCompleteListener {
            if(it.isSuccessful) {
                Log.e("update user name", "successful")
            }else {
                Log.e("update user name", "unsuccessful")
            }
        }.await()
    }

}

