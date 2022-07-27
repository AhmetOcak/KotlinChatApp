package com.ahmet.features.message

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.repository.FirebaseMessagesRepository
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.firebase.GetUserData
import com.ahmet.data.usecase.userdatabase.GetUserFromDb
import com.ahmet.domain.model.User
import com.ahmet.features.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getUser: GetUserData,
    private val getUserFromDb: GetUserFromDb,
    getCurrentUserEmail: GetCurrentUserEmail
) : BaseViewModel() {

    private val userEmail = MutableLiveData<String>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
    }

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _progressBarVisibility = MutableLiveData(View.INVISIBLE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private val _userFriends = MutableLiveData<MutableList<User>>(mutableListOf())
    val userFriends: LiveData<MutableList<User>> get() = _userFriends

    // if mail not null --> the user has not checked the remember me box.
    // if mail null --> the user has checked the remember me box.
    fun setUserData() {
        setProgBarVis(Status.LOADING)
        if (!userEmail.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _user.value = getUser.getUserDoc(userEmail.value.toString())
                getUserFriendsData()
            }
        } else {
            _user.value = getUserFromDb.getUser()
            getUserFriendsData()
        }
    }

    private fun getUserFriendsData() {
        viewModelScope.launch {
            val friend = FirebaseMessagesRepository().searchUserFriends(userEmail.value.toString())

            if (_user.value != null && _userFriends.value.isNullOrEmpty()) {
                for (i in 0 until (_user.value?.userFriends?.size ?: 0)) {
                    getUser.getUserDoc(_user.value!!.userFriends[i])
                        ?.let { _userFriends.value?.add(it) }
                }
                if (friend != "null") {
                    getUser.getUserDoc(friend)?.let {
                        _userFriends.value?.add(it)
                    }
                }
            }

            setProgBarVis(Status.DONE)
        }
    }

    suspend fun refreshUserFriends() {
        clearFriends()
        _user.value = getUser.getUserDoc(userEmail.value.toString())
        for (i in 0 until (_user.value?.userFriends?.size ?: 0)) {
            getUser.getUserDoc(_user.value!!.userFriends[i])
                ?.let { _userFriends.value?.add(it) }
        }
    }

    private fun setProgBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

    private fun clearFriends() {
        _userFriends.value?.clear()
    }
}

