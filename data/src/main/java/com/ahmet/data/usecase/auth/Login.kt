package com.ahmet.data.usecase.auth

import com.ahmet.data.repository.AuthRepository
import javax.inject.Inject

class Login @Inject constructor(private val repository: AuthRepository) {

    suspend fun login(email: String, password: String): String? {
        return repository.login(email, password)
    }
}