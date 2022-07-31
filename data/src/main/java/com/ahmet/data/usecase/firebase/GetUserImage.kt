package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class GetUserImage @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun getUserImage(email: String): String? = repository.getUserImage(email)
}