package com.ahmet.data.usecase

import com.ahmet.data.local.db.UserDatabase
import com.ahmet.data.local.db.entity.UserDataEntity
import javax.inject.Inject

class GetUserFromDb @Inject constructor(private val db: UserDatabase) {

    fun getUser(): UserDataEntity? {
        return db.userDao().getUser()
    }
}