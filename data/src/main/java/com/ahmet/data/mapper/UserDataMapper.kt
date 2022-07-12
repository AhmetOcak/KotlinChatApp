package com.ahmet.data.mapper

import com.ahmet.data.local.db.entity.UserDataEntity
import com.ahmet.domain.interfaces.IEntityMapper
import com.ahmet.domain.model.User

class UserDataMapper: IEntityMapper<UserDataEntity, User> {
    override fun mapFromEntity(entity: UserDataEntity): User {
        return User(
            entity.userName,
            entity.userEmail,
            entity.password
        )
    }

    override fun entityFromModel(model: User): UserDataEntity {
        return UserDataEntity(
            0,
            model.userName,
            model.emailAddress,
            model.password
        )
    }
}