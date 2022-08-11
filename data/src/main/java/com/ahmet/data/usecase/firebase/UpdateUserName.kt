package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class UpdateUserName @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun updateName(email: String, newUserName: String) {
        repository.updateUserName(email, newUserName)
    }
}