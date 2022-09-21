package com.ahmet.data.usecase.friend

import com.ahmet.data.repository.FriendRepository
import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class GetUserFriends @Inject constructor(private val repository: FriendRepository) {

    suspend fun getFriends(email: String): List<String> {
        return repository.getUserDoc(email)?.userFriends ?: listOf()
    }
}