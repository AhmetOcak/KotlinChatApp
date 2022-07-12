package com.ahmet.domain.interfaces

import com.ahmet.domain.model.User

interface IFirebaseUserDataRepository {

    suspend fun register(
        email: String,
        password: String,
        userName: String
    ): String?

    fun saveUserDoc(
        email: String,
        password: String,
        userName: String,
        collectionPath: String
    )

    suspend fun login(email: String, password: String): String?

    suspend fun getUserDoc(email: String): User?
}