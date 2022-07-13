package com.ahmet.data.model

data class UserEntity(
    val userName: String,
    val emailAddress: String,
    val password: String,
    val userFriends: List<String>
)

