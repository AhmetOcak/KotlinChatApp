package com.ahmet.features.message

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentMessagesBinding
import com.ahmet.features.dialogs.AddUserDialogFragment

class MessagesFragment : BaseFragment<MessageViewModel, FragmentMessagesBinding>() {

    override fun getViewModelClass() = MessageViewModel::class.java
    override fun getViewDataBinding() = FragmentMessagesBinding.inflate(layoutInflater)

    private val addUserDialogFragment = AddUserDialogFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackHandler()

        binding.currentUserImage.setOnClickListener {
            goToNextScreen(R.id.action_messagesFragment_to_accountSettingsFragment)
        }
        binding.addUser.setOnClickListener {
            addUserDialogFragment.show(parentFragmentManager, "Add User")
        }
    }

    private fun onBackHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityCompat.finishAffinity(requireActivity())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}