package com.ahmet.data.usecase.firebase

import com.ahmet.data.mapper.UserMapper
import com.ahmet.data.model.UserEntity
import com.ahmet.data.repository.FirebaseUserDataRepository
import com.ahmet.domain.model.User
import javax.inject.Inject

class GetUserData @Inject constructor(
    private val repository: FirebaseUserDataRepository,
    private val mapper: UserMapper
) {
    suspend fun getUserDoc(email: String): User {
        return repository.getUserDoc(email)!!
    }
}