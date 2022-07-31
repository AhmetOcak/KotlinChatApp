package com.ahmet.data.usecase.firebase

import android.net.Uri
import com.ahmet.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class UploadUserImage @Inject constructor(private val repository: FirebaseUserDataRepository) {
    suspend fun uploadUserImage(filePath: Uri): String? = repository.uploadImage(filePath)
}