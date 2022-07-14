package com.ahmet.data.usecase.userdatabase

import com.ahmet.data.local.db.UserDatabase
import com.ahmet.data.local.db.entity.UserDataEntity
import javax.inject.Inject

class UpdateUserDb @Inject constructor(private val db: UserDatabase) {

    fun updateUserDb(username: String, email: String, password: String, userFriends: List<String>) {
        db.userDao().updateUser(UserDataEntity(0, username, email, password, userFriends))
    }
}