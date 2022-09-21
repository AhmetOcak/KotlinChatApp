package com.ahmet.domain.interfaces

interface IAuthRepository {

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
}