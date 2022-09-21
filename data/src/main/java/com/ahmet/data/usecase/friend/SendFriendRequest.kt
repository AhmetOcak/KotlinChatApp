package com.ahmet.data.usecase.friend

import com.ahmet.data.repository.FriendRepository
import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class SendFriendRequest @Inject constructor(private val repository: FriendRepository) {
    suspend fun sendRequest(userEmail: String, friendEmail: String): String? =
        repository.sendFriendRequest(userEmail, friendEmail)
}