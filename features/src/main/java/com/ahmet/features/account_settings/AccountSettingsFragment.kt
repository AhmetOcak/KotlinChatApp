package com.ahmet.features.account_settings

import android.os.Bundle
import android.view.View
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.dialogs.DeleteAccountDialogFragment
import com.ahmet.features.dialogs.LogOutDialogFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentAccountSettingsBinding

class AccountSettingsFragment : BaseFragment<AccountSettingsViewModel, FragmentAccountSettingsBinding>() {

    private val logOut: LogOutDialogFragment = LogOutDialogFragment()
    private val deleteAccount: DeleteAccountDialogFragment = DeleteAccountDialogFragment()

    override fun getViewModelClass() = AccountSettingsViewModel::class.java
    override fun getViewDataBinding() = FragmentAccountSettingsBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            goToNextScreen(R.id.action_accountSettingsFragment_to_messagesFragment)
        }

        binding.logOutButton.setOnClickListener {
            logOut.show(parentFragmentManager, "Log Out")
        }

        binding.deleteAccount.setOnClickListener {
            deleteAccount.show(parentFragmentManager, "Delete Account")
        }
    }
}