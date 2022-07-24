package com.ahmet.features.chat

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.adapter.IncomingMessageAdapter
import com.ahmet.features.adapter.OutgoingMessageAdapter
import com.ahmet.features.databinding.FragmentChatBinding
import com.ahmet.features.utils.Constants
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {

    override fun getViewModelClass() = ChatViewModel::class.java
    override fun getViewDataBinding() = FragmentChatBinding.inflate(layoutInflater)

    lateinit var incomingMessageAdapter: IncomingMessageAdapter
    private lateinit var outgoingMessageAdapter: OutgoingMessageAdapter

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
                outgoingMessageAdapter.notifyDataSetChanged()
            }
        }
        viewModel.friendMessages.observe(viewLifecycleOwner) {
            if (viewModel.progressBarVisibility.value == View.INVISIBLE) {
                incomingMessageAdapter.notifyDataSetChanged()
            }

        }
    }

    private fun setupAdapters() {
        val incomingMA = FlexboxLayoutManager(activity)
        val outgoingMA = FlexboxLayoutManager(activity)

        binding.incomingMessageRecylerview.layoutManager = incomingMA
        incomingMA.flexDirection = FlexDirection.COLUMN
        incomingMA.justifyContent = JustifyContent.FLEX_START
        incomingMA.alignItems = AlignItems.FLEX_START

        binding.outgoingMessageRecylerview.layoutManager = outgoingMA
        outgoingMA.flexDirection = FlexDirection.COLUMN
        outgoingMA.justifyContent = JustifyContent.FLEX_START
        outgoingMA.alignItems = AlignItems.FLEX_END

        incomingMessageAdapter = IncomingMessageAdapter(viewModel.friendMessages.value ?: listOf())
        outgoingMessageAdapter = OutgoingMessageAdapter(viewModel.userMessages.value ?: listOf())

        binding.incomingMessageRecylerview.adapter = incomingMessageAdapter
        binding.outgoingMessageRecylerview.adapter = outgoingMessageAdapter
    }

}