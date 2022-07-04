package com.chatapp.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.chatapp.R
import com.chatapp.databinding.FragmentMessagesBinding
import com.chatapp.utils.Constants
import com.chatapp.ui.dialogs.AddUserDialogFragment

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private val addUserDialogFragment = AddUserDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackHandler()

        binding.currentUserImage.setOnClickListener {
            goToAccountSettingsScreen()
        }

        binding.addUser.setOnClickListener {
            addUserDialogFragment.show(parentFragmentManager, Constants.ADD_USER)
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

    private fun goToAccountSettingsScreen() {
        findNavController().navigate(R.id.action_messagesFragment_to_accountSettingsFragment)
    }
}