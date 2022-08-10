package com.ahmet.features.dialogs.adduser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ahmet.core.base.BaseViewModel
import com.ahmet.features.utils.helpers.EmailController
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.firebase.SendFriendRequest
import com.ahmet.features.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddUserDialogViewModel @Inject constructor(
    private val sendFriendRequest: SendFriendRequest,
    getCurrentUserEmail: GetCurrentUserEmail,
) : BaseViewModel() {

    val friendEmail = MutableLiveData<String?>()
    private val userEmail = MutableLiveData<String?>()
    var errorMessage = MutableLiveData<String?>()
    var firebaseMessage = MutableLiveData<String?>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
    }

    private fun checkEmailField(): Boolean {
        return if (friendEmail.value.isNullOrEmpty()) {
            errorMessage.value = Constants.EMPTY_FIELD_MESSAGE
            false
        } else {
            true
        }
    }

    private fun checkEmail(): Boolean {
        return if (EmailController.emailController(friendEmail.value.toString())) {
            true
        } else {
            errorMessage.value = Constants.EMAIL_MESSAGE
            false
        }
    }

    // user can't add himself/herself :D
    private fun isUserEmail(): Boolean {
        return if (userEmail.value == friendEmail.value) {
            errorMessage.value = "You can't add yourself"
            false
        } else {
            return true
        }
    }

    suspend fun sendFriendRequest() {
        try {
            if (checkEmailField() && checkEmail() && isUserEmail()) {
                firebaseMessage.value = sendFriendRequest.sendRequest(userEmail.value!!, friendEmail.value!!)
                clearFields()
            }
        } catch (e: Exception) {
            Log.e("send friend request exception", e.toString())
        }
    }

    private fun clearFields() {
        friendEmail.value = null
    }

}