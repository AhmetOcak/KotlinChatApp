package com.ahmet.features.account_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ahmet.features.dialogs.DeleteAccountDialogFragment
import com.ahmet.features.dialogs.LogOutDialogFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentAccountSettingsBinding

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
            logOut.show(parentFragmentManager, "Log Out")
        }

        binding.deleteAccount.setOnClickListener {
            deleteAccount.show(parentFragmentManager, "Delete Account")
        }
    }

    private fun goToMessagesScreen() {
        findNavController().navigate(R.id.action_accountSettingsFragment_to_messagesFragment)
    }
}