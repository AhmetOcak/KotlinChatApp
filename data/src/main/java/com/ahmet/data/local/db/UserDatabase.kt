package com.ahmet.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmet.data.local.db.dao.UserDao
import com.ahmet.data.local.db.entity.UserDataEntity
import com.ahmet.data.utils.converters.Converter

@Database(entities = [UserDataEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
}