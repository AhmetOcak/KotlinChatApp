package com.chatapp.ui.accountsettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chatapp.R
import com.chatapp.databinding.FragmentAccountSettingsBinding
import com.chatapp.utils.Constants
import com.chatapp.ui.dialogs.DeleteAccountDialogFragment
import com.chatapp.ui.dialogs.LogOutDialogFragment

class AccountSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAccountSettingsBinding
    private val logOut: LogOutDialogFragment = LogOutDialogFragment()
    private val deleteAccount: DeleteAccountDialogFragment = DeleteAccountDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            goToMessagesScreen()
        }

        binding.logOutButton.setOnClickListener {
            logOut.show(parentFragmentManager, Constants.LOG_OUT)
        }

        binding.deleteAccount.setOnClickListener {
            deleteAccount.show(parentFragmentManager, Constants.DELETE_ACCOUNT)
        }
    }

    private fun goToMessagesScreen() {
        findNavController().navigate(R.id.action_accountSettingsFragment_to_messagesFragment)
    }
}