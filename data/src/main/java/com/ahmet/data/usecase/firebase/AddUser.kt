package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class AddUser @Inject constructor(private val repository: FirebaseUserDataRepository){
    suspend fun addUser(friendEmail: String, userEmail: String): String? {
        return repository.addUser(friendEmail, userEmail)
    }
}