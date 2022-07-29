package com.ahmet.features.chat

import android.annotation.SuppressLint
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
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    getCurrentUserEmail: GetCurrentUserEmail,
    private val listenMessageData: ListenMessageData,
    private val sendMessage: SendMessage
) : BaseViewModel() {

    private val userEmail = MutableLiveData<String>()
    val friendEmail = MutableLiveData<String>()
    val friendUserName = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    init {
        userEmail.value = getCurrentUserEmail.getCurrentUser()
    }

    private val _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private val _messageData = MutableLiveData<Message>()
    val messageData: LiveData<Message> get() = _messageData

    private val _userMessages = MutableLiveData<MutableList<MutableMap<String, Any>>>()
    val userMessages: LiveData<MutableList<MutableMap<String, Any>>> get() = _userMessages

    private val _friendMessages = MutableLiveData<MutableList<MutableMap<String, Any>>>()
    val friendMessages: LiveData<MutableList<MutableMap<String, Any>>> get() = _friendMessages

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

    @SuppressLint("NewApi")
    fun sendMessageData() {
        if (checkMessage()) {
            viewModelScope.launch {
                try {
                    sendMessage.sendMessage(
                        message.value.toString(),
                        userEmail.value!!,
                        friendEmail.value!!,
                        Instant.now().epochSecond
                    )
                    clearChatField()
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
        _messageData.value = message ?: Message(mutableListOf(), mutableListOf())
        _userMessages.value = _messageData.value?.userMessage ?: mutableListOf()
        _friendMessages.value = _messageData.value?.friendMessage ?: mutableListOf()

        for (i in 0 until _userMessages.value!!.size) {
            _userMessages.value!![i]["isMe"] = true
        }
        setProgressBarVis(Status.DONE)
    }

    private fun checkMessage(): Boolean {
        return !(message.value.isNullOrEmpty() || message.value == "null" || message.value == " ")
    }

    private fun clearChatField() {
        message.value = ""
    }

}