package com.ahmet.features.edit_profile

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.firebase.GetUserImage
import com.ahmet.data.usecase.firebase.UploadUserImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val uploadUserImage: UploadUserImage,
    private val getUserImage: GetUserImage,
    private val sharedPreferences: SharedPreferences,
    private val getCurrentUserEmail: GetCurrentUserEmail
) : BaseViewModel() {

    val resultMessage = MutableLiveData<String>()
    val userImageFilePath = MutableLiveData<String>()

    private val _progBarVisibility = MutableLiveData(View.GONE)
    val progBarVisibility: LiveData<Int> get() = _progBarVisibility

    fun uploadUserImage(filePath: Uri) {
        _progBarVisibility.value = View.VISIBLE
        viewModelScope.launch {
            try {
                if (filePath != Uri.EMPTY) {
                    resultMessage.value = uploadUserImage.uploadUserImage(filePath)
                    _progBarVisibility.value = View.GONE
                } else {
                    Log.e("message", "uri is empty")
                }
            } catch (e: Exception) {
                Log.e("upload user image exception", e.toString())
                _progBarVisibility.value = View.GONE
            }
        }
    }

    fun getUserImage() {
        viewModelScope.launch {
            try {
                userImageFilePath.value = getUserImage.getUserImage(getCurrentUserEmail.getCurrentUser()!!)
                putSharedPref()
            } catch (e: Exception) {
                Log.e("get user image exception", e.toString())
            }
        }
    }

    private fun putSharedPref() {
        sharedPreferences.edit().putString(getCurrentUserEmail.getCurrentUser(), userImageFilePath.value).apply()
    }

    fun getUserImageFromSharedPref(): String? =
        sharedPreferences.getString(getCurrentUserEmail.getCurrentUser(), null)

}