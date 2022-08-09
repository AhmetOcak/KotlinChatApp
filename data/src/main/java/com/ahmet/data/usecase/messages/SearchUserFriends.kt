package com.ahmet.data.usecase.messages

import com.ahmet.data.repository.FirebaseMessagesRepository
import javax.inject.Inject

class SearchUserFriends @Inject constructor(private val repository: FirebaseMessagesRepository) {
    suspend fun searchFriends(userEmail: String): MutableList<String> =
        repository.searchUserFriends(userEmail)
}