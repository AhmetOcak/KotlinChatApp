package com.ahmet.data.usecase.friend

import com.ahmet.data.repository.FriendRepository
import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class DeleteFriendRequest @Inject constructor(private val repository: FriendRepository) {
    suspend fun deleteRequest(friendEmail: String, userEmail: String): String? =
        repository.deleteFriendRequest(friendEmail, userEmail)
}