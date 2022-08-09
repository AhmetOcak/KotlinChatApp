package com.ahmet.features.friend_requests

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.firebase.GetFriendRequests
import com.ahmet.data.usecase.firebase.GetUserImage
import com.ahmet.features.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestsViewModel @Inject constructor(
    private val getFriendRequests: GetFriendRequests,
    private val getCurrentUserEmail: GetCurrentUserEmail,
    private val sharedPreferences: SharedPreferences,
    private val getUserImage: GetUserImage
): BaseViewModel() {

    private val _userEmail = MutableLiveData<String>()

    private val _friendRequests = MutableLiveData<List<String>>(listOf())
    val friendRequests: LiveData<List<String>> get() = _friendRequests

    private val _progressBarVis = MutableLiveData(View.GONE)
    val progressBarVis: LiveData<Int> get() = _progressBarVis

    init {
        _userEmail.value = getCurrentUserEmail.getCurrentUser()
        getRequests()
    }

    private fun getRequests() {
        setProgressBarVis(Status.LOADING)
        viewModelScope.launch {
            _friendRequests.value = _userEmail.value?.let { getFriendRequests.getRequests(it) }
            putUserFriendsImages()
            setProgressBarVis(Status.DONE)
        }
    }

    private fun setProgressBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVis.value = View.VISIBLE
        else _progressBarVis.value = View.INVISIBLE
    }

    private suspend fun putUserFriendsImages() {
        for (i in 0 until (_friendRequests.value?.size ?: 0)) {
            try {
                val image = getUserImage.getUserImage(_friendRequests.value?.get(i)!!)
                if (image != null) {
                    sharedPreferences.edit().putString(_friendRequests.value?.get(i)!!, image).apply()
                }
            } catch (e: Exception) {
                Log.e("storage exception", e.toString())
            }
        }
    }
}