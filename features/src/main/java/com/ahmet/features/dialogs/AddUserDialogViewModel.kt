package com.ahmet.features.dialogs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.core.utils.EmailController
import com.ahmet.data.usecase.firebase.AddUser
import com.ahmet.data.usecase.userdatabase.GetUserFromDb
import com.ahmet.features.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddUserDialogViewModel @Inject constructor(
    private val addUser: AddUser,
    private val getUserFromDb: GetUserFromDb
) : BaseViewModel() {

    val friendEmail = MutableLiveData<String?>()
    val userEmail = MutableLiveData<String?>()
    var errorMessage = MutableLiveData<String?>()
    var firebaseMessage = MutableLiveData<String?>()

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
        return if(checkIsUserCached() == friendEmail.value.toString()) {
            errorMessage.value = "You can't add yourself"
            false
        }else {
            return true
        }
    }

    fun addFriend() {
        viewModelScope.launch {
            try {
                if (checkEmailField() && checkEmail() && isUserEmail()) {
                    firebaseMessage.value = addUser.addUser(
                        friendEmail.value.toString(),
                        checkIsUserCached()
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

    // if user clicked remember me box then the user is cached. The user email will received from database
    // if not then user not cached. The user email will come with arg
    private fun checkIsUserCached(): String {
        return if(userEmail.value.toString() != "null") {
            userEmail.value.toString()
        }else {
            getUserFromDb.getUser()?.emailAddress.toString()
        }
    }
}