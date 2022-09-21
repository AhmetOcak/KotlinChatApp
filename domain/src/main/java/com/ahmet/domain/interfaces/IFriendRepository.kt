package com.ahmet.domain.interfaces

import com.ahmet.domain.model.User

interface IFriendRepository {

    suspend fun getUserDoc(email: String): User?

    suspend fun sendFriendRequest(userEmail: String, friendEmail: String): String?

    suspend fun getFriendRequests(email: String): List<String>

    suspend fun addUser(friendEmail: String, userEmail: String): String?

    suspend fun deleteFriendRequest(friendEmail: String, userEmail: String): String?
}