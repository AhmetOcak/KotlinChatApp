package com.ahmet.data.usecase.friend

import com.ahmet.data.repository.FriendRepository
import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class AddUser @Inject constructor(private val repository: FriendRepository){
    suspend fun addUser(friendEmail: String, userEmail: String): String? {
        return repository.addUser(friendEmail, userEmail)
    }
}