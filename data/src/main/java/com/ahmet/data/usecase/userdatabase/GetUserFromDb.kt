package com.ahmet.data.usecase.userdatabase

import com.ahmet.data.local.db.UserDatabase
import com.ahmet.data.local.db.entity.UserDataEntity
import com.ahmet.data.mapper.UserDataMapper
import com.ahmet.domain.model.User
import javax.inject.Inject

class GetUserFromDb @Inject constructor(private val db: UserDatabase) {
    fun getUser(): UserDataEntity? {
        return db.userDao().getUser()
    }
}