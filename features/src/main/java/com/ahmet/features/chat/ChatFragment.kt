package com.ahmet.features.chat

import com.ahmet.core.base.BaseFragment
import com.ahmet.features.databinding.FragmentChatBinding

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>() {

    override fun getViewModelClass() = ChatViewModel::class.java
    override fun getViewDataBinding() = FragmentChatBinding.inflate(layoutInflater)

}