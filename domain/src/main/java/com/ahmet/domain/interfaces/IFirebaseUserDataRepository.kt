package com.ahmet.domain.interfaces

import android.net.Uri
import com.ahmet.domain.model.User

interface IFirebaseUserDataRepository {

    suspend fun register(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
    ): String?

    fun saveUserDoc(
        email: String,
        password: String,
        userName: String,
        userFriends: List<String>,
        collectionPath: String
    )

    suspend fun login(email: String, password: String): String?

    suspend fun getUserDoc(email: String): User?

    suspend fun addUser(friendEmail: String, userEmail: String): String?

    suspend fun deleteUser(): String?

    suspend fun deleteUserDoc(email: String) : Boolean

    suspend fun reauthenticate(email: String, password: String) : Boolean

    suspend fun uploadImage(filePath: Uri) : String?

    suspend fun getUserImage(email: String) : String?
}