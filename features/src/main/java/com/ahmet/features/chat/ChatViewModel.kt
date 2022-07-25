package com.ahmet.features.chat

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ahmet.core.base.BaseViewModel
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.messages.ListenMessageData
import com.ahmet.data.usecase.messages.SendMessage
import com.ahmet.domain.model.Message
import com.ahmet.features.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    getCurrentUserEmail: GetCurrentUserEmail,
    private val listenMessageData: ListenMessageData,
    private val sendMessage: SendMessage
) : BaseViewModel() {

    private val userEmail = MutableLiveData<String>()
    val friendEmail = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
    }

    private val _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private val _messageData = MutableLiveData<Message>()
    val messageData: LiveData<Message> get() = _messageData

    private val _userMessages = MutableLiveData<List<Map<String, Any>>>()
    val userMessages: LiveData<List<Map<String, Any>>> get() = _userMessages

    private val _friendMessages = MutableLiveData<List<Map<String, Any>>>()
    val friendMessages: LiveData<List<Map<String, Any>>> get() = _friendMessages

    fun getMessageData() {
        setProgressBarVis(Status.LOADING)

        viewModelScope.launch {
            listenMessageData.listenData(
                userEmail.value.toString(),
                friendEmail.value.toString(),
                ::callbackFun
            )
        }
    }

    fun sendMessageData() {
        if (checkMessage()) {
            viewModelScope.launch {
                try {
                    sendMessage.sendMessage(
                        message.value.toString(),
                        userEmail.value!!,
                        friendEmail.value!!,
                        1658742397
                    )
                } catch (e: Exception) {
                    Log.e("message send exception", e.message.toString())
                }
            }
        }
    }

    private fun setProgressBarVis(status: Status) {
        if (status == Status.LOADING) _progressBarVisibility.value = View.VISIBLE
        else _progressBarVisibility.value = View.INVISIBLE
    }

    private fun callbackFun(message: Message?) {
        _messageData.value = message ?: Message(listOf(), listOf())
        _userMessages.value = _messageData.value?.userMessage ?: listOf()
        _friendMessages.value = _messageData.value?.friendMessage ?: listOf()
        setProgressBarVis(Status.DONE)
    }

    private fun checkMessage(): Boolean {
        return !(message.value.isNullOrEmpty() || message.value == "null" || message.value == " ")
    }

}