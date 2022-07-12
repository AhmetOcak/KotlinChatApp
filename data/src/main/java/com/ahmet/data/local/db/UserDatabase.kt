package com.ahmet.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmet.data.local.db.dao.UserDao
import com.ahmet.data.local.db.entity.UserDataEntity

@Database(entities = [UserDataEntity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
}