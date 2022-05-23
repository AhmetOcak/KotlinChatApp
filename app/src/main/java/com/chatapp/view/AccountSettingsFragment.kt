package com.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chatapp.R
import com.chatapp.databinding.FragmentAccountSettingsBinding

class AccountSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAccountSettingsBinding
    private lateinit var dialog: SignOutDialogFragment

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

        binding.signOutButton.setOnClickListener {
            dialog = SignOutDialogFragment(
                getString(R.string.log_out_message),
                getString(R.string.log_out),
                getString(R.string.cancel)
            )
            dialog.show(parentFragmentManager, "Log Out")
        }

        binding.deleteAccount.setOnClickListener {
            dialog = SignOutDialogFragment(
                getString(R.string.delete_account_message),
                getString(R.string.delete_account),
                getString(R.string.cancel)
            )
            dialog.show(parentFragmentManager, "Delete Account")
        }
    }

    private fun goToMessagesScreen() {
        findNavController().navigate(R.id.action_accountSettingsFragment_to_messagesFragment)
    }

}