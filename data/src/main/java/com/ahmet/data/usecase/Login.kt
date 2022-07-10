package com.ahmet.data.usecase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class Login @Inject constructor(private val repository: FirebaseUserDataRepository) {

    suspend fun login(email: String, password: String): String? {
        return repository.login(email, password)
    }
}