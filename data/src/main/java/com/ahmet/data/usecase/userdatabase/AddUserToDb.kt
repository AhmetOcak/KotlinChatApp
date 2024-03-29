package com.ahmet.data.usecase.userdatabase

import android.util.Log
import com.ahmet.data.local.db.UserDatabase
import com.ahmet.data.local.db.entity.UserDataEntity
import javax.inject.Inject

class AddUserToDb @Inject constructor(private val db: UserDatabase) {

    fun addUser(userName: String, email: String, password: String, userFriends: List<String>, friendRequests: List<String>) {
        try {
            db.userDao().addUser(UserDataEntity(0, userName, email, password, userFriends, friendRequests))
        }catch (e: Exception){
            Log.e("db_exception", e.toString())
        }
    }
}