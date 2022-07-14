package com.ahmet.data.usecase.userdatabase

import com.ahmet.data.local.db.UserDatabase
import javax.inject.Inject

class DeleteUserFromDb @Inject constructor(private val db: UserDatabase) {

    fun deleteUserFromDb() {
        db.userDao().deleteUser()
    }
}