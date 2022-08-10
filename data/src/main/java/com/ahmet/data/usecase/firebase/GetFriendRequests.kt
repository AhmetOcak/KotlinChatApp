package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class GetFriendRequests @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun getRequests(email: String): List<String> = repository.getFriendRequests(email)
}