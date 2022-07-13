package com.ahmet.data.usecase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class Register @Inject constructor(private val repository: FirebaseUserDataRepository) {

    suspend fun register(email: String, password: String, username: String, userFriends: List<String>): String? {
        return repository.register(email, password, username, userFriends)
    }
}