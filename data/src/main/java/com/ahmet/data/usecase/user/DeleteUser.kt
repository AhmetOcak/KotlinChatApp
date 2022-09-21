package com.ahmet.data.usecase.user

import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class DeleteUser @Inject constructor(private val repository: UserRepository) {
    suspend fun deleteUser(): String? = repository.deleteUser()
}