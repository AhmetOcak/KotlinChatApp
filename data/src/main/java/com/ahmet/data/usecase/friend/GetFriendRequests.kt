package com.ahmet.data.usecase.friend

import com.ahmet.data.repository.FriendRepository
import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class GetFriendRequests @Inject constructor(private val repository: FriendRepository) {
    suspend fun getRequests(email: String): List<String> = repository.getFriendRequests(email)
}