package com.ahmet.data.usecase.user

import android.net.Uri
import com.ahmet.data.repository.UserRepository
import javax.inject.Inject

class UploadUserImage @Inject constructor(private val repository: UserRepository) {
    suspend fun uploadUserImage(filePath: Uri): String? = repository.uploadImage(filePath)
}