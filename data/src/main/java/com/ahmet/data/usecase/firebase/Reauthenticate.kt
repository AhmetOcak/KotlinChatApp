package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class Reauthenticate @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun reauthenticate(email: String, password: String): Boolean =
        repository.reauthenticate(email, password)

}