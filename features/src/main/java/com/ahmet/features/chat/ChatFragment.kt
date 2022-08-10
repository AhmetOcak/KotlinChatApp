package com.ahmet.features.chat

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.adapter.ChatAdapter
import com.ahmet.features.databinding.FragmentChatBinding
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.helpers.ImpUserImage
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {

    override fun getViewModelClass() = ChatViewModel::class.java
    override fun getViewDataBinding() = FragmentChatBinding.inflate(layoutInflater)

    private var chatAdapter: ChatAdapter? = null

    lateinit var chat: MutableList<MutableMap<String, Any>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.back.setOnClickListener {
            goToNextScreen(R.id.action_chatFragment_to_messagesFragment, null, null)
        }

        viewModel.friendEmail.value = requireArguments().getString(Constants.USER_FRIEND_ARG_KEY)
        viewModel.friendUserName.value = requireArguments().getString(Constants.USER_FRIEND_NAME_ARG_KEY)

        initUserImage()

        viewModel.listenMessageData()

        viewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                if (chatAdapter == null) {
                    setupAdapters()
                }
            }
        }

        observeChat()
    }

    private fun setupAdapters() {
        chat = viewModel.userMessages.value ?: mutableListOf()
        chat.addAll(viewModel.friendMessages.value ?: mutableListOf())
        chat.sortByDescending { (it["date"] as Timestamp).seconds }
        chat.reverse()

        binding.chatRecylerview.layoutManager = LinearLayoutManager(activity)
        chatAdapter = ChatAdapter(chat)
        binding.chatRecylerview.adapter = chatAdapter
    }

    private fun observeChat() {
        viewModel.userMessages.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                prepareChat()
            }
        }

        viewModel.friendMessages.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                prepareChat()
            }
        }
    }

    private fun prepareChat() {
        chat.clear()

        for (i in 0 until viewModel.userMessages.value!!.size) {
            viewModel.userMessages.value!![i]["isMe"] = true
        }

        chat.addAll(viewModel.userMessages.value ?: mutableListOf())
        chat.addAll(viewModel.friendMessages.value ?: mutableListOf())
        chat.sortByDescending { (it["date"] as Timestamp).seconds }
        chat.reverse()

        if (chat.size == 1) {
            chatAdapter?.notifyDataSetChanged();
        } else {
            chatAdapter?.notifyItemInserted(chat.size)
            binding.chatRecylerview.scrollToPosition(chat.size - 1)
        }
    }

    private fun initUserImage() {
        val path = viewModel.getUserImageFromSharedPref()
        if (path != null) {
            val bitmap = ImpUserImage.implementUserImage(path)
            binding.friendImage.setImageBitmap(bitmap)
        } else {
            binding.friendImage.setImageResource(com.ahmet.core.R.drawable.blank_profile_picture)
        }
    }

}