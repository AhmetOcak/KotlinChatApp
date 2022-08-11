package com.ahmet.domain.interfaces

import android.net.Uri
import com.ahmet.domain.model.User

interface IFirebaseUserDataRepository {

    fun getCurrentUserEmail(): String?

    suspend fun reauthenticate(email: String, password: String): Boolean

    suspend fun register(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
        friendRequests: List<String>
    ): String?

    suspend fun login(email: String, password: String): String?

    fun saveUserDoc(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
        friendRequests: List<String>,
        collectionPath: String
    )

    suspend fun getUserDoc(email: String): User?

    suspend fun sendFriendRequest(userEmail: String, friendEmail: String): String?

    suspend fun getFriendRequests(email: String): List<String>

    suspend fun addUser(friendEmail: String, userEmail: String): String?

    suspend fun deleteFriendRequest(friendEmail: String, userEmail: String): String?

    suspend fun deleteUser(): String?

    suspend fun deleteUserDoc(email: String): Boolean

    suspend fun uploadImage(filePath: Uri): String?

    suspend fun getUserImage(email: String): String?

    suspend fun updateUserName(email: String, newUserName: String)

}