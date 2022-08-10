package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class SendFriendRequest @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun sendRequest(userEmail: String, friendEmail: String): String? =
        repository.sendFriendRequest(userEmail, friendEmail)
}