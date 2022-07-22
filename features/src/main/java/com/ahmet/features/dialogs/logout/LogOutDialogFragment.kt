package com.ahmet.features.dialogs.logout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ahmet.core.base.BaseDialogFragment
import com.ahmet.data.usecase.userdatabase.DeleteUserFromDb
import com.ahmet.features.R
import com.ahmet.features.databinding.CustomLogoutDialogBinding
import com.ahmet.features.utils.Constants
import javax.inject.Inject

class LogOutDialogFragment @Inject constructor() :
    BaseDialogFragment<LogOutViewModel, CustomLogoutDialogBinding>() {

    override fun getViewModelClass(): Class<LogOutViewModel> = LogOutViewModel::class.java
    override fun getViewDataBinding(): CustomLogoutDialogBinding =
        CustomLogoutDialogBinding.inflate(layoutInflater)

    @Inject
    lateinit var deleteUserFromDb: DeleteUserFromDb
    @Inject
    lateinit var toast: Toast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.dialogLogOutButtonDAD.setOnClickListener {
            deleteUserFromDb.deleteUserFromDb()
            showToastMessage()
            goToLoginScreen()
            dismiss()
        }

        binding.dialogCancelButtonDAD.setOnClickListener {
            dismiss()
        }
    }

    private fun goToLoginScreen() {
        findNavController().navigate(
            R.id.action_accountSettingsFragment_to_loginFragment,
            Bundle().apply { putBoolean(Constants.IS_COME_FROM_APP, true) })
    }

    override fun showToastMessage() {
        toast.setText("You have successfully logged out")
        toast.show()
    }
}

