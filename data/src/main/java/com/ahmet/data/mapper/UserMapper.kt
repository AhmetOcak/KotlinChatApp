package com.ahmet.data.mapper

import com.ahmet.data.model.UserEntity
import com.ahmet.domain.interfaces.IEntityMapper
import com.ahmet.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor() : IEntityMapper<UserEntity, User> {
    override fun mapFromEntity(entity: UserEntity): User {
        return User(
            entity.userName,
            entity.emailAddress,
            entity.password,
            entity.userFriends
        )
    }

    override fun entityFromModel(model: User): UserEntity {
        return UserEntity(
            model.userName,
            model.emailAddress,
            model.password,
            model.userFriends
        )
    }
}