package com.ahmet.data.usecase.user

import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserEmail @Inject constructor(private val repository: UserRepository) {
    fun getCurrentUser(): String? = repository.getCurrentUserEmail()
}