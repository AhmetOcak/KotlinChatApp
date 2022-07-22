package com.ahmet.data.usecase.firebase

import com.ahmet.data.repository.FirebaseUserDataRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GetCurrentUserEmail @Inject constructor(private val repository: FirebaseUserDataRepository) {
    fun getCurrentUser(): String? = repository.getCurrentUserEmail()
}