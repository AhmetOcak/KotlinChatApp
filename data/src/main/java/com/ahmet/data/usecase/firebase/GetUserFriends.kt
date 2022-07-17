package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class GetUserFriends @Inject constructor(private val repository: FirebaseUserDataRepository) {

    suspend fun getFriends(email: String): List<String> {
        return repository.getUserDoc(email)?.userFriends ?: listOf()
    }
}