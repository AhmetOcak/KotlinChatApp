package com.ahmet.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmet.data.utils.UserDatabase

@Entity(tableName = UserDatabase.TABLE_NAME)
data class UserDataEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "user_email")
    var userEmail: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "user_friends")
    var userFriends: List<String>
)
