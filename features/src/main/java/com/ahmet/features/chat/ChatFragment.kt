package com.ahmet.features.chat

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.adapter.ChatAdapter
import com.ahmet.features.databinding.FragmentChatBinding
import com.ahmet.features.utils.Constants
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {

    override fun getViewModelClass() = ChatViewModel::class.java
    override fun getViewDataBinding() = FragmentChatBinding.inflate(layoutInflater)

    private lateinit var chatAdapter: ChatAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.friendEmail.value = requireArguments().getString(Constants.USER_FRIEND_ARG_KEY)

        viewModel.getMessageData()
        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                setupAdapters()
            }
        }

        viewModel.userMessages.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                chatAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupAdapters() {
        val chat: MutableList<MutableMap<String, Any>> = viewModel.userMessages.value ?: mutableListOf()
        chat.addAll(viewModel.friendMessages.value ?: mutableListOf())
        chat.sortByDescending { (it["date"] as Timestamp).seconds }
        chat.reverse()

        binding.chatRecylerview.layoutManager = LinearLayoutManager(activity)
        chatAdapter = ChatAdapter(chat)
        binding.chatRecylerview.adapter = chatAdapter
    }

}