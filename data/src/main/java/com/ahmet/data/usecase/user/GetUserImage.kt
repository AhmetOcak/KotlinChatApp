package com.ahmet.data.usecase.user

import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class GetUserImage @Inject constructor(private val repository: UserRepository) {
    suspend fun getUserImage(email: String): String? = repository.getUserImage(email)
}