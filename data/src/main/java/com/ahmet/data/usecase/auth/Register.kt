package com.ahmet.data.usecase.auth

import com.ahmet.data.repository.AuthRepository
import javax.inject.Inject

class Register @Inject constructor(private val repository: AuthRepository) {

    suspend fun register(
        email: String,
        password: String,
        username: String,
        userFriends: List<String>,
        friendRequests: List<String>
    ): String? {
        return repository.register(email, password, username, userFriends, friendRequests)
    }
}