package com.ahmet.features.register

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.usecase.FirebaseOperations
import com.ahmet.features.utils.CollectionPaths
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.Status
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel : BaseViewModel() {

    val userName = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()

    var message = MutableLiveData<String?>()
    var firebaseMessage = MutableLiveData<String?>()

    private val _progressBarVisibility = MutableLiveData(View.INVISIBLE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private fun checkTextFields(): Boolean {
        return if (!userName.value.isNullOrEmpty() && !email.value.isNullOrEmpty() && !password.value.isNullOrEmpty() && !confirmPassword.value.isNullOrEmpty()) {
            true
        } else {
            message.value = Constants.EMPTY_FIELD_MESSAGE
            false
        }
    }

    private fun checkEmail(): Boolean {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()) {
            true
        } else {
            message.value = Constants.EMAIL_MESSAGE
            false
        }
    }

    private fun checkPassword(): Boolean {
        return if (password.value?.length!! < 6) {
            message.value = Constants.PASSWORD_LENGTH_MESSAGE
            false
        } else if (!password.value.equals(confirmPassword.value)) {
            message.value = Constants.PASSWORD_MATCH_MESSAGE
            false
        } else {
            true
        }
    }

    private fun checkRegisterInfo(): Boolean = checkTextFields() && checkEmail() && checkPassword()

    fun register() {
        viewModelScope.launch {
            try {
                if (checkRegisterInfo()) {
                    setProgBarVis(Status.LOADING)
                    firebaseMessage.value = FirebaseOperations().register(
                        email.value.toString(),
                        password.value.toString(),
                        userName.value.toString(),
                        CollectionPaths.USER
                    )
                    clearFields()
                }
            } catch (e: Exception) {
                setProgBarVis(Status.ERROR)
                firebaseMessage.value = e.message
            }
        }
    }

    private fun clearFields() {
        userName.value = null
        email.value = null
        password.value = null
        confirmPassword.value = null
        message.value = null
    }

    private fun setProgBarVis(status: Status) {
        if(status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

}