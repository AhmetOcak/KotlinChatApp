package com.ahmet.features.login

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.core.utils.EmailController
import com.ahmet.data.mapper.UserMapper
import com.ahmet.data.usecase.auth.Login
import com.ahmet.data.usecase.firebase.GetUserData
import com.ahmet.data.usecase.userdatabase.AddUserToDb
import com.ahmet.data.usecase.userdatabase.DeleteUserFromDb
import com.ahmet.data.usecase.userdatabase.GetUserFromDb
import com.ahmet.data.usecase.userdatabase.UpdateUserDb
import com.ahmet.domain.model.User
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginFirebase: Login,
    private val userData: GetUserData,
    private val addUserToDb: AddUserToDb,
    private val updateUserDb: UpdateUserDb,
    private val getUserDb: GetUserFromDb,
    private val deleteUserFromDb: DeleteUserFromDb
) : BaseViewModel() {

    private val _progressBarVisibility = MutableLiveData(View.INVISIBLE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    val email = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()
    val rememberMeCheckBox = MutableLiveData(false)

    var message = MutableLiveData<String?>()
    var firebaseMessage = MutableLiveData<String?>()
    var status = MutableLiveData<Status>()

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
                    status.value = Status.LOADING
                    setProgBarVis(Status.LOADING)
                    firebaseMessage.value =
                        loginFirebase.login(
                            email.value.toString(),
                            password.value.toString()
                        )

                    val user = getUserData()
                    if (rememberMeCheckBox.value == true) {
                        if (user != null) {
                            if (getUserDb.getUser() != null) {
                                updateUserDb.updateUserDb(
                                    user.userName,
                                    user.emailAddress,
                                    user.password,
                                    user.userFriends
                                )
                            } else {
                                addUserToDb.addUser(
                                    user.userName,
                                    user.emailAddress,
                                    user.password,
                                    user.userFriends
                                )
                            }
                        } else {
                            throw Exception()
                        }
                    } else {
                        if (getUserDb.getUser() != null) {
                            deleteUserFromDb.deleteUserFromDb()
                        }
                    }

                    status.value = Status.DONE
                    setProgBarVis(Status.DONE)
                    message.value = null
                }
            } catch (e: Exception) {
                firebaseMessage.value = e.message
                status.value = Status.ERROR
                setProgBarVis(Status.ERROR)
            }
        }
    }

    private suspend fun getUserData(): User? {
        var user: User? = null

        try {
            user = userData.getUserDoc(email.value.toString())
        } catch (e: Exception) {
            Log.e("exception", e.message.toString())
        }

        return user
    }

    private fun setProgBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

    fun isUserCached(): Boolean {
        return getUserDb.getUser() != null
    }

}