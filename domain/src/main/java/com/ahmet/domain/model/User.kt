package com.ahmet.domain.model

data class User(
    var userName: String,
    val emailAddress: String,
    val password: String,
    val userFriends: List<String>,
    val friendRequest: List<String>,
)
