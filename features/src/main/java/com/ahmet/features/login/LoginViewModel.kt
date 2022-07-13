package com.ahmet.features.login

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.core.utils.EmailController
import com.ahmet.data.mapper.UserMapper
import com.ahmet.data.usecase.*
import com.ahmet.domain.model.User
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.Status
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginFirebase: Login,
    private val userData: GetUserData,
    private val mapper: UserMapper,
    private val addUserToDb: AddUserToDb,
    private val updateUserDb: UpdateUserDb,
    private val getUserDb: GetUserFromDb
) : BaseViewModel() {

    private val _progressBarVisibility = MutableLiveData(View.INVISIBLE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    val email = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()
    val rememberMeCheckBox = MutableLiveData(false)

    var message = MutableLiveData<String?>()
    var firebaseMessage = MutableLiveData<String?>()

    private fun checkFields(): Boolean {
        return if (!email.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) {
            true
        } else {
            message.value = Constants.EMPTY_FIELD_MESSAGE
            false
        }
    }

    private fun checkEmail(): Boolean {
        return if (EmailController.emailController(email.value.toString())) {
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
        } else {
            true
        }
    }

    private fun checkLoginInfo(): Boolean = checkFields() && checkEmail() && checkPassword()

    fun login() {
        viewModelScope.launch {
            try {
                if (checkLoginInfo()) {
                    setProgBarVis(Status.LOADING)
                    firebaseMessage.value =
                        loginFirebase.login(
                            email.value.toString(),
                            password.value.toString()
                        )

                    val user = getUserData()
                    Log.e("user", user.toString())
                    if (rememberMeCheckBox.value == true) {
                        if (user != null) {
                            if (getUserDb.getUser() != null) {
                                updateUserDb.updateUserDb(
                                    user.userName,
                                    user.emailAddress,
                                    user.password
                                )
                            } else {
                                addUserToDb.addUser(user.userName, user.emailAddress, user.password)
                            }
                        } else {
                            throw Exception()
                        }
                    }

                    clearFields()
                    setProgBarVis(Status.DONE)
                }
            } catch (e: Exception) {
                firebaseMessage.value = e.message
                Log.e("loginerror", e.toString())
                setProgBarVis(Status.ERROR)
            }
        }
    }

    private suspend fun getUserData(): User? {
        var user: User? = null

        try {
            user = mapper.mapFromEntity(userData.getUserDoc(email.value.toString()))
        } catch (e: Exception) {
            Log.e("exception", e.message.toString())
        }

        return user
    }

    private fun setProgBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

    private fun clearFields() {
        email.value = null
        password.value = null
        message.value = null
    }

}