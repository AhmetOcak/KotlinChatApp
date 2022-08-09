package com.ahmet.data.model

data class UserEntity(
    var userName: String,
    val emailAddress: String,
    val password: String,
    val userFriends: List<String>,
    val friendRequests: List<String>
)

