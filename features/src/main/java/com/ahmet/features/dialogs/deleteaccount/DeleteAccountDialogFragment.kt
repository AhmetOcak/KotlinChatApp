package com.ahmet.features.dialogs.deleteaccount

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ahmet.core.base.BaseDialogFragment
import com.ahmet.data.usecase.userdatabase.DeleteUserFromDb
import com.ahmet.data.usecase.userdatabase.GetUserFromDb
import com.ahmet.features.R
import com.ahmet.features.databinding.CustomDeleteAccountDialogBinding
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.FirebaseCommonMessages
import com.ahmet.features.utils.FirebaseDeleteMessages
import com.ahmet.features.utils.ResultMessages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeleteAccountDialogFragment @Inject constructor() :
    BaseDialogFragment<DeleteAccountDialogViewModel, CustomDeleteAccountDialogBinding>() {

    override fun getViewModelClass(): Class<DeleteAccountDialogViewModel> = DeleteAccountDialogViewModel::class.java
    override fun getViewDataBinding(): CustomDeleteAccountDialogBinding = CustomDeleteAccountDialogBinding.inflate(layoutInflater)

    @Inject
    lateinit var getUserFromDb: GetUserFromDb
    @Inject
    lateinit var deleteUserFromDb: DeleteUserFromDb
    @Inject
    lateinit var toast: Toast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.dialogDeleteAccountButtonDAD.setOnClickListener {
            viewModel.deleteUserAccount()

            viewModel.resultMessage.observe(viewLifecycleOwner) {
                if (viewModel.resultMessage.value == ResultMessages.SUCCESSFUL) {
                    if (getUserFromDb.getUser() != null) deleteUserFromDb.deleteUserFromDb()
                    toast.setText(FirebaseDeleteMessages.DEL_SUCCESSFUL)
                    toast.show()

                    goToLoginScreen()
                    dismiss()
                }
            }
            showToastMessage()
        }

        binding.dialogCancelButtonDAD.setOnClickListener {
            dismiss()
        }
    }

    private fun goToLoginScreen() {
        findNavController().navigate(R.id.action_accountSettingsFragment_to_loginFragment)
    }

    override fun showToastMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            when (viewModel.errorMessage.value) {
                FirebaseCommonMessages.NETWORK_ERROR -> {
                    toast.setText(FirebaseCommonMessages.NETWORK_ERROR)
                    toast.show()
                }
                FirebaseDeleteMessages.WRONG_PASSWORD -> {
                    toast.setText(FirebaseDeleteMessages.WRONG_PASSWORD)
                    toast.show()
                }
                Constants.EMPTY_FIELD_MESSAGE -> {
                    toast.setText(Constants.EMPTY_FIELD_MESSAGE)
                    toast.show()
                }
                else -> {
                    toast.setText(FirebaseCommonMessages.UNKNOWN_ERROR)
                    toast.show()
                }
            }
        }
    }

}