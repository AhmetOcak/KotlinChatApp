package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class DeleteUser @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun deleteUser(): String? = repository.deleteUser()
}