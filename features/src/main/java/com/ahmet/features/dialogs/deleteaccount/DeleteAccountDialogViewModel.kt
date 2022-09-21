package com.ahmet.features.dialogs.deleteaccount

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.usecase.user.DeleteUser
import com.ahmet.data.usecase.user.DeleteUserDoc
import com.ahmet.data.usecase.user.GetCurrentUserEmail
import com.ahmet.data.usecase.auth.Reauthenticate
import com.ahmet.features.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DeleteAccountDialogViewModel @Inject constructor(
    private val deleteUser: DeleteUser,
    private val reauthenticate: Reauthenticate,
    private val deleteUserDoc: DeleteUserDoc,
    getCurrentUserEmail: GetCurrentUserEmail
) : BaseViewModel() {

    private val userEmail = MutableLiveData<String?>()

    var resultMessage = MutableLiveData<String?>()
    var errorMessage = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
    }

    fun deleteUserAccount() {
        if(checkPasswordField()) {
            viewModelScope.launch {
                try {
                    if (reauthenticate.reauthenticate(userEmail.value.toString(), userPassword.value.toString())) {
                        if (deleteUserDoc.deleteUserDoc(userEmail.value.toString())) {
                            viewModelScope.launch {
                                resultMessage.value = deleteUser.deleteUser()
                            }
                        }
                    }
                } catch (e: Exception) {
                    errorMessage.value = e.message
                    Log.e("delete excpetion", e.message.toString())
                }
            }
        }else {
            errorMessage.value = Constants.EMPTY_FIELD_MESSAGE
        }
    }

    private fun checkPasswordField() : Boolean = !userPassword.value.isNullOrEmpty()
}