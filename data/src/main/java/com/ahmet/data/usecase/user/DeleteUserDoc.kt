package com.ahmet.data.usecase.user

import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class DeleteUserDoc @Inject constructor(private val repository: UserRepository) {
    suspend fun deleteUserDoc(email: String) : Boolean = repository.deleteUserDoc(email)
}