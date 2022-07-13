package com.ahmet.data.local.db.dao

import androidx.room.*
import com.ahmet.data.local.db.entity.UserDataEntity
import com.ahmet.data.utils.UserDatabase

@Dao
interface UserDao {

    @Insert
    fun addUser(user: UserDataEntity?)

    @Update
    fun updateUser(user: UserDataEntity?)

    @Query("SELECT * FROM user")
    fun getUser(): UserDataEntity?

    @Query("DELETE FROM ${UserDatabase.TABLE_NAME}")
    fun deleteUser()
}