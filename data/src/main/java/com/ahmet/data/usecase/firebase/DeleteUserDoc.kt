package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class DeleteUserDoc @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun deleteUserDoc(email: String) : Boolean = repository.deleteUserDoc(email)
}