package com.ahmet.features.chat

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.messages.ListenMessageData
import com.ahmet.domain.model.Message
import com.ahmet.features.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    getCurrentUserEmail: GetCurrentUserEmail,
    private val listenMessageData: ListenMessageData
) : BaseViewModel() {

    val friendEmail = MutableLiveData<String>()
    private val userEmail = MutableLiveData<String>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
    }

    private val _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private val _messages = MutableLiveData<Message>()
    val messages: LiveData<Message> get() = _messages

    private val _userMessages = MutableLiveData<List<Map<String, Any>>>()
    val userMessages: LiveData<List<Map<String, Any>>> get() = _userMessages

    private val _friendMessages = MutableLiveData<List<Map<String, Any>>>()
    val friendMessages: LiveData<List<Map<String, Any>>> get() = _friendMessages

    fun getMessageData() {
        setProgressBarVis(Status.LOADING)

        listenMessageData.listenData(
            userEmail.value.toString(),
            friendEmail.value.toString(),
            ::callbackFun
        )
    }

    private fun setProgressBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

    private fun callbackFun(message: Message?) {
        _messages.value = message ?: Message(listOf(), listOf())
        _userMessages.value = _messages.value?.userMessage ?: listOf()
        _friendMessages.value = _messages.value?.friendMessage ?: listOf()
        setProgressBarVis(Status.DONE)
    }

}