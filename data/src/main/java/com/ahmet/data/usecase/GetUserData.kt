package com.ahmet.data.usecase

import com.ahmet.data.mapper.UserMapper
import com.ahmet.data.model.UserEntity
import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class GetUserData @Inject constructor(
    private val repository: FirebaseUserDataRepository,
    private val mapper: UserMapper
) {
    suspend fun getUserDoc(email: String): UserEntity {
        return mapper.entityFromModel(repository.getUserDoc(email)!!)
    }
}