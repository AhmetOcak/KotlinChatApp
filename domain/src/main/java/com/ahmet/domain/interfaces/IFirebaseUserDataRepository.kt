package com.ahmet.domain.interfaces

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
}