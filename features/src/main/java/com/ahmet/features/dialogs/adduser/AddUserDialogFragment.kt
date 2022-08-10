package com.ahmet.features.dialogs.adduser

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ahmet.core.base.BaseDialogFragment
import com.ahmet.features.databinding.CustomAdduserDialogBinding
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.FirebaseCommonMessages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddUserDialogFragment @Inject constructor() :
    BaseDialogFragment<AddUserDialogViewModel, CustomAdduserDialogBinding>() {

    override fun getViewModelClass(): Class<AddUserDialogViewModel> = AddUserDialogViewModel::class.java
    override fun getViewDataBinding(): CustomAdduserDialogBinding = CustomAdduserDialogBinding.inflate(layoutInflater)

    @Inject
    lateinit var toast: Toast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.dialogAddUserButtonAUD.setOnClickListener {
            lifecycleScope.launch {
                viewModel.sendFriendRequest()
                showToastMessage()
                viewModel.errorMessage.value = null
            }
        }
        observeFirebaseResponse()
        viewModel.firebaseMessage.value = null

        binding.dialogCancelButtonAUD.setOnClickListener { dismiss() }
    }

    override fun showToastMessage() {
        when {
            viewModel.errorMessage.value.toString() == Constants.EMPTY_FIELD_MESSAGE -> {
                toast.setText(Constants.EMPTY_FIELD_MESSAGE)
                toast.show()
            }
            viewModel.errorMessage.value.toString() == Constants.EMAIL_MESSAGE -> {
                toast.setText(Constants.EMAIL_MESSAGE)
                toast.show()
            }
            viewModel.errorMessage.value.toString() == "You can't add yourself" -> {
                toast.setText("You can't add yourself")
                toast.show()
            }
        }
    }

    private fun observeFirebaseResponse() {
        viewModel.firebaseMessage.observe(viewLifecycleOwner) {
            when {
                viewModel.firebaseMessage.value.toString() == FirebaseCommonMessages.NETWORK_ERROR -> {
                    toast.setText(FirebaseCommonMessages.NETWORK_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == FirebaseCommonMessages.UNKNOWN_ERROR -> {
                    toast.setText(FirebaseCommonMessages.UNKNOWN_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == "You already added this account" -> {
                    toast.setText("You already added this account")
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == "There is no such account" -> {
                    toast.setText("There is no such account")
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == "Successful" -> {
                    toast.setText("Friend request sent successfully")
                    toast.show()
                    binding.dialogAddUserButtonAUD.hideKeyboard()
                }
                viewModel.firebaseMessage.value.toString() == "Unsuccessful" -> {
                    toast.setText("Something went wrong. Please try again later.")
                    toast.show()
                }
            }
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}