package com.ahmet.data.usecase.user

import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserName @Inject constructor(private val repository: UserRepository) {
    suspend fun updateName(email: String, newUserName: String) {
        repository.updateUserName(email, newUserName)
    }
}