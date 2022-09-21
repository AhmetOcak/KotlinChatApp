package com.ahmet.data.usecase.friend

import com.ahmet.data.mapper.UserMapper
import com.ahmet.data.model.UserEntity
import com.ahmet.data.repository.FriendRepository
import javax.inject.Inject

class GetUserData @Inject constructor(private val repository: FriendRepository, private val mapper: UserMapper) {
    suspend fun getUserDoc(email: String): UserEntity? {
        return repository.getUserDoc(email)?.let { mapper.entityFromModel(it) }
    }
}