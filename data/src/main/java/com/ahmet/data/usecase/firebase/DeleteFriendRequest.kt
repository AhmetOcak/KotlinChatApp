package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class DeleteFriendRequest @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun deleteRequest(friendEmail: String, userEmail: String): String? =
        repository.deleteFriendRequest(friendEmail, userEmail)
}