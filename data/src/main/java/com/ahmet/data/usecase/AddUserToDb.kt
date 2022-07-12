package com.ahmet.data.usecase

import android.util.Log
import com.ahmet.data.local.db.UserDatabase
import com.ahmet.data.local.db.entity.UserDataEntity
import javax.inject.Inject

class AddUserToDb @Inject constructor(private val db: UserDatabase) {

    fun addUser(userName: String, email: String, password: String) {
        try {
            db.userDao().addUser(UserDataEntity(0, userName, email, password))
        }catch (e: Exception){
            Log.e("db_exception", e.toString())
        }
    }
}