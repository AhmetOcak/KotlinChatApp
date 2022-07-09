package com.ahmet.domain.interfaces

interface IFirebaseOperations {

    suspend fun register(
        email: String,
        password: String,
        userName: String,
        collectionPath: String
    ): String?

    fun saveFirestore(
        email: String,
        password: String,
        userName: String,
        collectionPath: String
    )

    suspend fun login(email: String, password: String): String?

    suspend fun readFirestore(collectionPath: String)
}