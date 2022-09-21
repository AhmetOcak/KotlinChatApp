package com.ahmet.domain.interfaces

import android.net.Uri
import com.ahmet.domain.model.User

interface IUserRepository {

    fun getCurrentUserEmail(): String?

    suspend fun deleteUser(): String?

    suspend fun deleteUserDoc(email: String): Boolean

    suspend fun uploadImage(filePath: Uri): String?

    suspend fun getUserImage(email: String): String?

    suspend fun updateUserName(email: String, newUserName: String)

}