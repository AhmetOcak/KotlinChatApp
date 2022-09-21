package com.ahmet.data.usecase.auth

import com.ahmet.data.repository.AuthRepository
import javax.inject.Inject

class Reauthenticate @Inject constructor(private val repository: AuthRepository) {
    suspend fun reauthenticate(email: String, password: String): Boolean =
        repository.reauthenticate(email, password)

}