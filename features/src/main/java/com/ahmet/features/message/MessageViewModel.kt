package com.ahmet.features.message

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.repository.FirebaseMessagesRepository
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.firebase.GetUserData
import com.ahmet.data.usecase.firebase.GetUserImage
import com.ahmet.data.usecase.messages.ListenAllMessageData
import com.ahmet.data.usecase.userdatabase.GetUserFromDb
import com.ahmet.data.utils.EditEmail
import com.ahmet.domain.model.User
import com.ahmet.features.utils.Status
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getUser: GetUserData,
    private val getUserFromDb: GetUserFromDb,
    private val getCurrentUserEmail: GetCurrentUserEmail,
    private val sharedPreferences: SharedPreferences,
    private val getUserImage: GetUserImage,
    private val listenAllMessageData: ListenAllMessageData
) : BaseViewModel() {

    val userImageFilePath = MutableLiveData<String>()
    val currentMessages = MutableLiveData<List<DocumentSnapshot>?>()
    private val tempMessages = MutableLiveData<List<DocumentSnapshot?>>()
    val unreadMessagesAlert = MutableLiveData<MutableList<Int>>(mutableListOf())

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _progressBarVisibility = MutableLiveData(View.INVISIBLE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private val _userFriends = MutableLiveData<MutableList<User>>(mutableListOf())
    val userFriends: LiveData<MutableList<User>> get() = _userFriends

    private val userEmail = MutableLiveData<String>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
        setUserData()
        listenData()
    }

    private fun listenData() {
        listenAllMessageData.listenMessagesData(
            getCurrentUserEmail.getCurrentUser()!!,
            ::callbackFun
        )
    }

    // receives data from the listener
    private fun callbackFun(message: List<DocumentSnapshot>) {
        currentMessages.value = message

        Log.e("AA", "worked")

        if(!_userFriends.value.isNullOrEmpty()) {
            if(tempMessages.value.isNullOrEmpty()) {
                tempMessages.value = currentMessages.value
                receiveUnreadMessages()
            }else {
                receiveUnreadMessages()
            }
        }

        tempMessages.value = currentMessages.value
    }

    // kullanıcı listesi alfabetik sıralamada gider
    private fun receiveUnreadMessages() {
        for (i in 0 until (_userFriends.value?.size ?: 0)) {
            val currentFriendEmail = EditEmail.removeDot(_userFriends.value!![i].emailAddress)
            val currentMessagesSize = (currentMessages.value!![i][currentFriendEmail] as MutableList<MutableMap<String, Any>>).size
            val tempMessagesSize = (tempMessages.value!![i]?.get(currentFriendEmail) as MutableList<MutableMap<String, Any>>).size
            val count = currentMessagesSize - tempMessagesSize

            Log.e("count", unreadMessagesAlert.value.toString())

            // if not 0 then there are unread messages
            if(unreadMessagesAlert.value.isNullOrEmpty() || unreadMessagesAlert.value!!.size < _userFriends.value!!.size) {
                unreadMessagesAlert.value?.add(i, count)
            }else {
                if(unreadMessagesAlert.value!![i] == 0) {
                    unreadMessagesAlert.value!![i] = count
                }
            }
        }
    }

    // if mail not null --> the user has not checked the remember me box.
    // if mail null --> the user has checked the remember me box.
    private fun setUserData() {
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

    // [EN] If user friends are not empty, we don't need to retrieve user friends data recursively.
    // [TR] userFriends value'sünün null ya da boş olup olmamasını kontrol ederek
    // sayfa değişiklikleri sırasında verinin tekrar tekrar alınmasını engelliyoruz.
    private fun getUserFriendsData() {
        if (_userFriends.value.isNullOrEmpty()) {
            viewModelScope.launch {
                val friend =
                    FirebaseMessagesRepository().searchUserFriends(userEmail.value.toString())

                if (_user.value != null && _userFriends.value.isNullOrEmpty()) {
                    for (i in 0 until (_user.value?.userFriends?.size ?: 0)) {
                        getUser.getUserDoc(_user.value!!.userFriends[i])
                            ?.let { _userFriends.value?.add(it) }
                    }
                    if (friend.isNotEmpty()) {
                        for (element in friend) {
                            getUser.getUserDoc(element)?.let {
                                it.userName = it.userName + " (not your friend)"
                                _userFriends.value?.add(it)
                            }
                        }
                    }

                    putUserFriendsImages()
                }
                setProgBarVis(Status.DONE)
            }
        } else {
            setProgBarVis(Status.DONE)
        }
    }

    suspend fun refreshUserFriends() {
        val friend = FirebaseMessagesRepository().searchUserFriends(userEmail.value.toString())
        clearFriends()

        _user.value = getUser.getUserDoc(userEmail.value.toString())
        for (i in 0 until (_user.value?.userFriends?.size ?: 0)) {
            getUser.getUserDoc(_user.value!!.userFriends[i])
                ?.let { _userFriends.value?.add(it) }
        }
        if (friend.isNotEmpty()) {
            for (element in friend) {
                getUser.getUserDoc(element)?.let {
                    it.userName = it.userName + " (not your friend)"
                    _userFriends.value?.add(it)
                }
            }
        }

        putUserFriendsImages()
    }

    private suspend fun putUserFriendsImages() {
        for (i in 0 until (_userFriends.value?.size ?: 0)) {
            try {
                val image = getUserImage.getUserImage(_userFriends.value?.get(i)!!.emailAddress)
                if (image != null) {
                    sharedPreferences.edit()
                        .putString(_userFriends.value?.get(i)!!.emailAddress, image).apply()
                }
            } catch (e: Exception) {
                Log.e("storage exception", e.toString())
            }
        }
    }

    fun getUserImage() {
        viewModelScope.launch {
            try {
                userImageFilePath.value =
                    getUserImage.getUserImage(getCurrentUserEmail.getCurrentUser()!!)
            } catch (e: java.lang.Exception) {
                Log.e("get user image exception", e.toString())
            }
        }
    }

    private fun setProgBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

    private fun clearFriends() {
        _userFriends.value?.clear()
    }

    fun putSharedPref() {
        sharedPreferences.edit()
            .putString(getCurrentUserEmail.getCurrentUser(), userImageFilePath.value).apply()
    }

    fun getUserImageFromSharedPref(): String? =
        sharedPreferences.getString(getCurrentUserEmail.getCurrentUser(), null)
}

