package com.ahmet.features.dialogs.adduser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.core.utils.EmailController
import com.ahmet.data.usecase.firebase.AddUser
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.messages.CreateMessageDoc
import com.ahmet.data.usecase.userdatabase.GetUserFromDb
import com.ahmet.features.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddUserDialogViewModel @Inject constructor(
    private val addUser: AddUser,
    private val getUserFromDb: GetUserFromDb,
    private val createMessageDoc: CreateMessageDoc,
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

    fun addFriend() {
        viewModelScope.launch {
            try {
                if (checkEmailField() && checkEmail() && isUserEmail()) {
                    firebaseMessage.value = addUser.addUser(
                        friendEmail.value.toString(),
                        userEmail.value.toString()
                    )
                    createMessageDoc.createMessageDoc(
                        userEmail.value.toString(),
                        friendEmail.value.toString()
                    )
                    clearFields()
                }
            } catch (e: Exception) {
                Log.e("add user exception", e.toString())
            }
        }
    }

    private fun clearFields() {
        friendEmail.value = null
    }

}